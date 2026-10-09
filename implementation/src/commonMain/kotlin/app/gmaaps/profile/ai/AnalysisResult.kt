package app.gmaaps.profile.ai

import app.gmaaps.core.data.model.BS
import app.gmaaps.core.data.model.CA
import app.gmaaps.core.data.model.GV
import kotlinx.serialization.Serializable

/**
 * ????????? ????????? ?????? ??? AI-???????.
 */
@Serializable
data class AnalysisResult(
    val analysisPeriodMs: Long,
    val analysisDays: Int,
    
    // ==================== ??????? ====================
    val avgGlucose: Double,
    val medianGlucose: Double,
    val glucoseStdDev: Double,
    val cvPercent: Double,
    
    // ==================== TIR / TAR / TBR ====================
    val tirPercent: Double,
    val tbrPercent: Double,
    val tarPercent: Double,
    
    // ==================== Bolus ? Carbs ====================
    val totalBoluses: Int,
    val totalBolusUnits: Double,
    val totalCarbsGrams: Double,
    val avgCarbsPerMeal: Double,
    
    // ==================== ??????? ====================
    val unexplainedSpikesCount: Int,
    val totalSpikesCount: Int,
    val maxGlucose: Double,
    val minGlucose: Double,
    
    // ==================== ???????? CGM ====================
    val cgmCoveragePercent: Double,
    val totalBgReadingsCount: Int,
    
    // ==================== ???????? ====================
    val estimatedA1c: Double?,
    val recentTrend: TrendDirection = TrendDirection.STABLE
) {

    enum class TrendDirection {
        UP, STABLE, DOWN
    }

    /** JSON-????????????? ???? ?????? ??? ???????? ?? AI-?????? */
    fun toJsonPayload(): String {
        return """{
            "analysisDays": $analysisDays,
            "avgGlucose": $avgGlucose,
            "medianGlucose": $medianGlucose,
            "glucoseStdDev": $glucoseStdDev,
            "cvPercent": $cvPercent,
            "tirPercent": $tirPercent,
            "tbrPercent": $tbrPercent,
            "tarPercent": $tarPercent,
            "totalBoluses": $totalBoluses,
            "totalBolusUnits": $totalBolusUnits,
            "totalCarbsGrams": $totalCarbsGrams,
            "avgCarbsPerMeal": $avgCarbsPerMeal,
            "unexplainedSpikesCount": $unexplainedSpikesCount,
            "totalSpikesCount": $totalSpikesCount,
            "maxGlucose": $maxGlucose,
            "minGlucose": $minGlucose,
            "cgmCoveragePercent": $cgmCoveragePercent,
            "estimatedA1c": ${estimatedA1c?.toString() ?: "null"},
            "recentTrend": "$recentTrend",
            "totalBgReadingsCount": $totalBgReadingsCount
        }""".trimIndent()
    }

    fun isSufficientData(config: AiProfileConfig): Boolean {
        return totalBgReadingsCount > 0 &&
               cgmCoveragePercent >= config.minCoveragePercent &&
               analysisDays >= config.minDataDays &&
               cvPercent < 50.0
    }
}


