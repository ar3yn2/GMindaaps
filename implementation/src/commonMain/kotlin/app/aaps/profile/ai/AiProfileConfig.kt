package app.aaps.profile.ai

import kotlinx.serialization.Serializable

/**
 * ???????????? AI-??????????? ???????.
 */
enum class AiAggressiveness {
    CALM,
    MODERATE,
    AGGRESSIVE;

    fun maxIsfShiftPercent(): Float = when (this) {
        CALM -> 0.05f
        MODERATE -> 0.15f
        AGGRESSIVE -> 0.25f
    }

    fun maxBasalShiftPercent(): Float = when (this) {
        CALM -> 0.05f
        MODERATE -> 0.10f
        AGGRESSIVE -> 0.20f
    }

    fun minConfidenceThreshold(): Float = when (this) {
        CALM -> 0.90f
        MODERATE -> 0.75f
        AGGRESSIVE -> 0.60f
    }

    fun maxSpikesWithoutCarbs(): Int = when (this) {
        CALM -> 1
        MODERATE -> 2
        AGGRESSIVE -> 4
    }
}

@Serializable
data class AiProfileConfig(
    val aggressiveness: AiAggressiveness = AiAggressiveness.MODERATE,
    val apiUrl: String = "https://api.yourai.com/v1/analyze",
    val apiKey: String = "",
    val analysisDays: Int = 7,
    val minCoveragePercent: Float = 0.8f,
    val spikeThreshold: Int = 30,
    val maxSpikesPerDay: Int = 2,
    val spikeLookbackMinutes: Int = 90,
    val minDataDays: Int = 3,
    val updateIntervalHours: Int = 24,
    val allowSameDayCorrection: Boolean = false
) {
    companion object {
        fun defaultConfig(): AiProfileConfig = AiProfileConfig()

        fun fromJson(json: String): AiProfileConfig {
            return try {
                kotlinx.serialization.json.Json.decodeFromString(serializer(), json)
            } catch (e: Exception) {
                defaultConfig()
            }
        }

        fun toJson(config: AiProfileConfig): String = 
            kotlinx.serialization.json.Json.encodeToString(serializer(), config)
    }
}

