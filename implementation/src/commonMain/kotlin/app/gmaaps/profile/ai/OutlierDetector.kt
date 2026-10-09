package app.gmaaps.profile.ai

import app.gmaaps.core.data.model.BS
import app.gmaaps.core.data.model.CA
import app.gmaaps.core.data.model.GV
import app.gmaaps.core.interfaces.db.PersistenceLayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OutlierDetector(
    private val persistence: PersistenceLayer
) {
    
    suspend fun detectAnomalies(config: AiProfileConfig): AnomalyResult = withContext(Dispatchers.IO) {
        val endTime = System.currentTimeMillis()
        val startTime = endTime - (config.analysisDays.toLong() * 24L * 60 * 60 * 1000)

        val bgReadings = persistence.getBgReadingsDataFromTimeToTime(startTime, endTime, false)
            .filter { it.isValid && it.value > 20 && it.value < 600 }
        
        val boluses = persistence.getBolusesFromTime(startTime, true)
            .filter { it.isValid && it.amount > 1.0 }
        
        val carbs = persistence.getCarbsFromTimeExpanded(startTime, true)
            .filter { it.isValid && it.amount > 5.0 }

        val spikeInfoList = SpikeInfo.detectFromReadings(
            bgReadings = bgReadings, boluses = boluses, carbs = carbs,
            spikeThreshold = config.spikeThreshold.toDouble(),
            maxLookbackMinutes = config.spikeLookbackMinutes.toLong()
        )

        val spikesByDay = groupSpikesByDay(spikeInfoList)
        
        val daysWithTooManyUnexplainedSpikes = spikesByDay.count { daySpikes ->
            val unexplainedCount = daySpikes.count { !it.isExplained() }
            unexplainedCount > config.maxSpikesPerDay
        }

        val anomalyType = when {
            daysWithTooManyUnexplainedSpikes >= 3 -> AnomalyResult.AnomalyType.HEAVY_ANOMALIES
            daysWithTooManyUnexplainedSpikes >= 1 -> AnomalyResult.AnomalyType.MODERATE_ANOMALIES
            else -> AnomalyResult.AnomalyType.NORMAL
        }

        val totalMinutes = if (bgReadings.isNotEmpty()) {
            (bgReadings.last().timestamp - bgReadings.first().timestamp) / (60 * 1000.0)
        } else 0.0
        
        val expectedReadings = totalMinutes / 5.0
        val cgmCoverage = if (expectedReadings > 0) {
            (bgReadings.size / expectedReadings).toFloat()
        } else 0f

        AnomalyResult(
            anomalyType = anomalyType,
            totalSpikesCount = spikeInfoList.size,
            unexplainedSpikesCount = spikeInfoList.count { !it.isExplained() },
            explainedSpikesCount = spikeInfoList.count { it.isExplained() },
            daysWithAnomalies = daysWithTooManyUnexplainedSpikes,
            analysisDays = config.analysisDays,
            cgmCoveragePercent = cgmCoverage,
            isSufficientData = bgReadings.isNotEmpty() && 
                               cgmCoverage >= 0.8f && 
                               config.analysisDays >= config.minDataDays
        )
    }

    private fun groupSpikesByDay(spikes: List<SpikeInfo>): List<List<SpikeInfo>> {
        if (spikes.isEmpty()) return emptyList()
        
        val minTs = spikes.minOf { it.timestamp }
        val daySize = 24L * 60 * 60 * 1000L
        val groups = mutableMapOf<Long, MutableList<SpikeInfo>>()

        for (spike in spikes) {
            val dayKey = ((spike.timestamp - minTs) / daySize).toLong() * daySize + minTs
            if (!groups.containsKey(dayKey)) groups[dayKey] = mutableListOf()
            groups[dayKey]!!.add(spike)
        }

        return groups.values.toList()
    }

    data class AnomalyResult(
        val anomalyType: AnomalyType,
        val totalSpikesCount: Int,
        val unexplainedSpikesCount: Int,
        val explainedSpikesCount: Int,
        val daysWithAnomalies: Int,
        val analysisDays: Int,
        val cgmCoveragePercent: Float,
        val isSufficientData: Boolean
    ) {
        enum class AnomalyType {
            NORMAL,
            MODERATE_ANOMALIES,
            HEAVY_ANOMALIES
        }

        companion object { 
            fun noAnomalies(): AnomalyResult = AnomalyResult(
                AnomalyResult.AnomalyType.NORMAL, 0, 0, 0, 0, 1, 100f, true
            ) 
        }

        fun canRecommend(): Boolean {
            return isSufficientData && anomalyType != AnomalyResult.AnomalyType.HEAVY_ANOMALIES
        }

        fun getUserMessage(): String? {
            return when (anomalyType) {
                AnomalyResult.AnomalyType.NORMAL -> null
                AnomalyResult.AnomalyType.MODERATE_ANOMALIES -> 
                    "Обнаружены умеренные аномалии glucose. Анализ проведён с осторожностью."
                AnomalyResult.AnomalyType.HEAVY_ANOMALIES -> 
                    "Много необъяснимых скачков glucose. Вероятно болезнь или ошибка CGM. Коррекция отложена."
            }
        }
    }

    suspend fun hasRecentAnomalies(config: AiProfileConfig): Boolean = withContext(Dispatchers.IO) {
        val endTime = System.currentTimeMillis()
        val startTime = endTime - (24L * 60 * 60 * 1000)

        val bgReadings = persistence.getBgReadingsDataFromTimeToTime(startTime, endTime, false)
            .filter { it.isValid && it.value > 20 && it.value < 600 }

        if (bgReadings.size < 5) return@withContext true

        val boluses = persistence.getBolusesFromTime(startTime, true)
            .filter { it.isValid && it.amount > 1.0 }

        val carbs = persistence.getCarbsFromTimeExpanded(startTime, true)
            .filter { it.isValid && it.amount > 5.0 }

        val spikes = SpikeInfo.detectFromReadings(
            bgReadings, boluses, carbs, 
            config.spikeThreshold.toDouble(), 
            config.spikeLookbackMinutes.toLong()
        )

        val unexplainedCount = spikes.count { !it.isExplained() }
        unexplainedCount > config.maxSpikesPerDay
    }
}

