package app.aaps.profile.ai

import kotlinx.serialization.Serializable

/**
 * ????????? ??????? AI — ???????????? ?? ????????????? ???????.
 */
@Serializable
data class ProfileRecommendation(
    val isfShiftPercent: Float,
    val basalShiftPercent: Float,
    val targetBgShift: Int? = null,
    val confidence: Float,
    val reason: String,
    val projectedA1cChange: Double? = null,
    val contributingFactors: List<String> = emptyList()
) {
    companion object {
        fun noChange(reason: String): ProfileRecommendation = 
            ProfileRecommendation(0f, 0f, null, 1.0f, reason, emptyList())

        fun isValid(recommendation: ProfileRecommendation, config: AiProfileConfig): Boolean {
            val agg = config.aggressiveness
            return kotlin.math.abs(recommendation.isfShiftPercent) <= agg.maxIsfShiftPercent() &&
                   kotlin.math.abs(recommendation.basalShiftPercent) <= agg.maxBasalShiftPercent() &&
                   recommendation.confidence >= agg.minConfidenceThreshold() &&
                   recommendation.confidence > 0.3f
        }
    }

    /** ????????? ???????? ? ???????? ??????? (??????????? ?????????). */
    fun applyToProfile(
        currentIsf: Double,
        currentBasalRate: Double,
        currentTargetBg: Int? = null
    ): ProfileRecommendation {
        val newIsf = currentIsf * (1.0 + isfShiftPercent.toDouble())
        val newBasal = currentBasalRate * (1.0 + basalShiftPercent.toDouble())
        val newTarget = targetBgShift?.let { currentTargetBg!! + it }

        return ProfileRecommendation(
            isfShiftPercent, basalShiftPercent, targetBgShift, confidence,
            reason = "Applied: ISF=%.2f, Basal=%.2f U/h".format(newIsf, newBasal),
            projectedA1cChange, contributingFactors
        )
    }

    /** ???????????? ????????? BG ??? ?????????? ????????????. */
    fun simulateBgEffect(
        currentBasalRate: Double, // units/hour
        isf: Double,              // mg/dL per unit
        hours: Int = 24
    ): Double {
        val basalChangeUnitsPerHour = currentBasalRate * basalShiftPercent.toDouble()
        return -basalChangeUnitsPerHour * isf * hours
    }
}

