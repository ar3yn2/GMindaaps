package app.gmaaps.profile.ai

import app.gmaaps.core.data.model.BS
import app.gmaaps.core.data.model.CA
import app.gmaaps.core.data.model.GV
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.notifications.NotificationManager
import app.gmaaps.core.interfaces.notifications.NotificationId
import app.gmaaps.core.interfaces.plugin.PluginBase
import app.gmaaps.core.interfaces.plugin.PluginDescription
import app.gmaaps.core.interfaces.resources.TextResolver
import kotlinx.coroutines.*
import kotlin.math.abs

class AiProfilePlugin(
    pluginDescription: PluginDescription,
    aapsLogger: AAPSLogger,
    override val rh: TextResolver,
    notificationManager: NotificationManager,
    private val persistence: PersistenceLayer,
    private val configStore: AiConfigStore,
    private val aiApiClient: AiApiClient
) : PluginBase(pluginDescription, aapsLogger, rh, notificationManager) {

    companion object {
        private const val TAG = "AIProfilePlugin"
        private const val CHECK_INTERVAL_MS = 6L * 60 * 60 * 1000
        @Volatile var cachedRecommendation: ProfileRecommendation? = null
    }

    private val dataAggregator = DataAggregator(persistence)
    private val outlierDetector = OutlierDetector(persistence)

    override suspend fun onStart() {
        aapsLogger.debug(LTag.APS, "$TAG: AI Profile Plugin started")
        pluginScope.launch {
            while (isActive) {
                withContext(Dispatchers.Default) { checkAndAnalyze() }
                delay(CHECK_INTERVAL_MS)
            }
        }
    }

    override suspend fun onStop() {
        aapsLogger.debug(LTag.APS, "$TAG: AI Profile Plugin stopped")
    }

    private var lastCheckTime: Long = 0L

    private suspend fun checkAndAnalyze() {
        try {
            val now = System.currentTimeMillis()
            if (now - lastCheckTime < CHECK_INTERVAL_MS) return
            
            lastCheckTime = now

            val config = configStore.getConfig()
            aapsLogger.debug(LTag.APS, "$TAG: AI Profile analysis check")

            if (!config.allowSameDayCorrection && wasRecommendationToday()) {
                aapsLogger.debug(LTag.APS, "$TAG: Skipping: already recommended today")
                return
            }

            val result = analyzeAndRecommend(config)
            
            when (result) {
                is AnalysisOutcome.Success -> {
                    if (result.recommendation != null) {
                        cachedRecommendation = result.recommendation
                        showNotification(result.recommendation, config)
                    } else {
                        aapsLogger.debug(LTag.APS, "$TAG: AI: profile is optimal")
                    }
                }
                is AnalysisOutcome.Skipped -> {
                    aapsLogger.debug(LTag.APS, "$TAG: Analysis skipped: ${result.reason}")
                }
                is AnalysisOutcome.Error -> {
                    if (result.cause != null) {
                        aapsLogger.error(LTag.APS, "$TAG: Analysis failed: ${result.message}", result.cause)
                    } else {
                        aapsLogger.error(LTag.APS, "$TAG: Analysis failed: ${result.message}")
                    }
                    if (result.isCritical) {
                        notificationManager.post(NotificationId.AI_PROFILE_ERROR, "AI Error: " + result.message)
                    }
                }
            }
        } catch (e: Exception) {
            aapsLogger.error(LTag.APS, "$TAG: Unexpected error in AI analysis", e)
        }
    }

    private suspend fun analyzeAndRecommend(config: AiProfileConfig): AnalysisOutcome {
        val aggregationResult = runCatching {
            dataAggregator.collectAnalysisData(config)
        }

        if (aggregationResult.isFailure) {
            return AnalysisOutcome.Error(
                message = "Error collecting data: ${aggregationResult.exceptionOrNull()?.message}",
                cause = aggregationResult.exceptionOrNull(),
                isCritical = true
            )
        }

        val data = aggregationResult.getOrNull() ?: return AnalysisOutcome.Skipped("No data")

        if (!data.isSufficientData(config)) {
            return AnalysisOutcome.Skipped(
                "Insufficient data: CGM coverage=${data.cgmCoveragePercent}%, days=$config.analysisDays, minDataDays=${config.minDataDays}"
            )
        }

        val anomalyResult = runCatching { outlierDetector.detectAnomalies(config) }
        
        if (anomalyResult.isFailure) {
            return AnalysisOutcome.Error(
                message = "Error detecting anomalies: ${anomalyResult.exceptionOrNull()?.message}",
                cause = anomalyResult.exceptionOrNull(),
                isCritical = false
            )
        }

        val anomalies = anomalyResult.getOrNull() ?: return AnalysisOutcome.Skipped("Anomalies")

        if (!anomalies.canRecommend()) {
            aapsLogger.debug(LTag.APS, "$TAG: Anomalies detected: ${anomalies.getUserMessage()}")
            return AnalysisOutcome.Skipped(anomalies.getUserMessage() ?: "Has anomalies")
        }

        val rawRecommendation = aiApiClient.generateRecommendation(
            apiConfig = config,
            analysisResult = data,
            anomalies = anomalies
        )

        if (rawRecommendation.isNoChange()) {
            return AnalysisOutcome.Success(null)
        }

        aapsLogger.debug(LTag.APS, "$TAG: Raw recommendation: ISF=${rawRecommendation.isfShiftPercent}, basal=${rawRecommendation.basalShiftPercent}")

        val simulationResult = simulateProfileChange(data, rawRecommendation)
        
        if (simulationResult.predictionBad) {
            aapsLogger.debug(LTag.APS, "$TAG: Simulation failed: predicted glucose=${simulationResult.predictedAvgGlucose}")
            val reducedConfidence = rawRecommendation.confidence * 0.5f
            if (reducedConfidence < config.aggressiveness.minConfidenceThreshold()) {
                return AnalysisOutcome.Skipped("Simulation: profile will get worse")
            }
        }

        aapsLogger.debug(LTag.APS, "$TAG: AI recommendation: ISF=${rawRecommendation.isfShiftPercent}, basal=${rawRecommendation.basalShiftPercent}, confidence=${rawRecommendation.confidence}")

        return AnalysisOutcome.Success(rawRecommendation)
    }

    private fun simulateProfileChange(
        data: app.gmaaps.profile.ai.AnalysisResult,
        recommendation: ProfileRecommendation
    ): SimulationResult {
        val estimatedTdd = estimateCurrentTdd(data)
        if (estimatedTdd <= 0.1f) return SimulationResult(predictionBad = true, predictedAvgGlucose = data.avgGlucose, estimatedTdd = estimatedTdd, newBasalRate = 0f)

        val newBasalRate = estimatedTdd * (1 + recommendation.basalShiftPercent)
        
        val isfMultiplier = 1.0 + recommendation.isfShiftPercent.toDouble()
        val basalChangePercent = recommendation.basalShiftPercent.toDouble()
        val estimatedBasalChangeUnits = estimatedTdd * basalChangePercent
        
        val predictedBgChange = -estimatedBasalChangeUnits * data.avgGlucose / 20.0 * isfMultiplier
        val predictedAvgGlucose = data.avgGlucose + predictedBgChange

        val predictionBad = when {
            predictedAvgGlucose < 50 -> true
            predictedAvgGlucose > 300 -> true  
            abs(predictedBgChange) > 100 -> true
            else -> false
        }

        return SimulationResult(predictionBad, predictedAvgGlucose, estimatedTdd, newBasalRate)
    }

    private fun estimateCurrentTdd(data: app.gmaaps.profile.ai.AnalysisResult): Float {
        val bolusUnits = data.totalBolusUnits.toFloat()
        
        val basalEstimate = if (data.totalBoluses > 2) {
            bolusUnits * 0.8f
        } else {
            bolusUnits * 1.5f
        }

        return maxOf(bolusUnits + basalEstimate, 2.0f)
    }

    private fun showNotification(recommendation: ProfileRecommendation, config: AiProfileConfig) {
        val bgChange = if (recommendation.isfShiftPercent < 0f) {
            "ISF -${(recommendation.isfShiftPercent * 100).toInt()}%"
        } else if (recommendation.isfShiftPercent > 0f) {
            "ISF +${(recommendation.isfShiftPercent * 100).toInt()}%"
        } else ""

        val basalChange = if (recommendation.basalShiftPercent != 0f) {
            "Basal ${if (recommendation.basalShiftPercent > 0f) "+" else ""}${(recommendation.basalShiftPercent * 100).toInt()}%"
        } else ""

        var message = "AI: "
        if (bgChange.isNotBlank()) message += bgChange
        if (basalChange.isNotBlank()) {
            if (message.length > 3) message += ", "
            message += basalChange
        }
        message += "\nConfidence: ${(recommendation.confidence * 100).toInt()}%"
        
        if (recommendation.isfShiftPercent != 0f || recommendation.basalShiftPercent != 0f) {
            message += "\n${recommendation.reason}"
        }

        notificationManager.post(NotificationId.AI_PROFILE_RECOMMENDATION, message)

        cachedRecommendation = recommendation.copy(confidence = -1f)
    }

    private fun wasRecommendationToday(): Boolean {
        return cachedRecommendation != null && cachedRecommendation!!.confidence > 0f
    }

    suspend fun forceAnalyze(): Result<ProfileRecommendation?> {
        return runCatching {
            val config = configStore.getConfig()
            analyzeAndRecommend(config).let { outcome ->
                when (outcome) {
                    is AnalysisOutcome.Success -> outcome.recommendation
                    else -> null
                }
            }
        }
    }

    sealed class AnalysisOutcome {
        data class Success(val recommendation: ProfileRecommendation?) : AnalysisOutcome()
        data class Skipped(val reason: String) : AnalysisOutcome()
        data class Error(val message: String, val cause: Throwable? = null, val isCritical: Boolean = false) : AnalysisOutcome()
    }

    data class SimulationResult(
        val predictionBad: Boolean,
        val predictedAvgGlucose: Double,
        val estimatedTdd: Float,
        val newBasalRate: Float
    )
    
    private suspend fun getAnalysisDays(): Int { return configStore.getConfig().analysisDays }
}

