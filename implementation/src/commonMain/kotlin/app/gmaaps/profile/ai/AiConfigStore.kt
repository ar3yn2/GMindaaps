package app.gmaaps.profile.ai

import app.gmaaps.core.keys.IntNonKey
import app.gmaaps.core.keys.StringNonKey
import app.gmaaps.core.keys.BooleanNonKey
import app.gmaaps.core.keys.interfaces.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.concurrent.Volatile

class AiConfigStore(
    private val preferences: Preferences
) {
    companion object {
        // Default values matching IntNonKey and StringNonKey defaults
        private const val DEFAULT_API_URL = "https://api.yourai.com/v1/analyze"
        private const val DEFAULT_AGGRESSIVENESS = "MODERATE"
    }

    @Volatile private var currentConfig: AiProfileConfig? = null

    suspend fun getConfig(): AiProfileConfig {
        return withContext(Dispatchers.IO) {
            currentConfig?.let { return@withContext it }

            val config = AiProfileConfig(
                aggressiveness = parseAggressiveness(),
                apiUrl = preferences.get(StringNonKey.AiApiUrl).takeIf { it.isNotBlank() } ?: DEFAULT_API_URL,
                apiKey = preferences.get(StringNonKey.AiApiKey),
                analysisDays = preferences.get(IntNonKey.AiAnalysisDays).takeIf { it in setOf(7, 30) } ?: 7,
                minCoveragePercent = (preferences.get(IntNonKey.AiMinCoveragePercent) / 100f).coerceIn(0.1f, 0.99f),
                spikeThreshold = preferences.get(IntNonKey.AiSpikeThreshold).coerceIn(15, 60),
                maxSpikesPerDay = preferences.get(IntNonKey.AiMaxSpikesPerDay).coerceIn(1, 5),
                spikeLookbackMinutes = preferences.get(IntNonKey.AiSpikeLookbackMinutes),
                minDataDays = preferences.get(IntNonKey.AiMinDataDays),
                updateIntervalHours = preferences.get(IntNonKey.AiUpdateIntervalHours).coerceIn(12, 72),
                allowSameDayCorrection = preferences.get(BooleanNonKey.AiAllowSameDayCorrection)
            )
            currentConfig = config
            config
        }
    }

    suspend fun updateConfig(config: AiProfileConfig) {
        withContext(Dispatchers.IO) {
            preferences.put(StringNonKey.AiApiUrl, config.apiUrl)
            preferences.put(StringNonKey.AiAggressiveness, config.aggressiveness.name)
            preferences.put(StringNonKey.AiApiKey, config.apiKey)
            preferences.put(IntNonKey.AiAnalysisDays, config.analysisDays)
            preferences.put(IntNonKey.AiMinCoveragePercent, (config.minCoveragePercent * 100f).toInt())
            preferences.put(IntNonKey.AiSpikeLookbackMinutes, config.spikeLookbackMinutes)
            preferences.put(IntNonKey.AiMinDataDays, config.minDataDays)
            preferences.put(IntNonKey.AiSpikeThreshold, config.spikeThreshold)
            preferences.put(IntNonKey.AiMaxSpikesPerDay, config.maxSpikesPerDay)
            preferences.put(IntNonKey.AiUpdateIntervalHours, config.updateIntervalHours)
            preferences.put(BooleanNonKey.AiAllowSameDayCorrection, config.allowSameDayCorrection)

            currentConfig = AiProfileConfig(
                aggressiveness = config.aggressiveness,
                apiUrl = config.apiUrl,
                apiKey = config.apiKey,
                analysisDays = config.analysisDays,
                minCoveragePercent = config.minCoveragePercent,
                spikeThreshold = config.spikeThreshold,
                maxSpikesPerDay = config.maxSpikesPerDay,
                spikeLookbackMinutes = config.spikeLookbackMinutes,
                minDataDays = config.minDataDays,
                updateIntervalHours = config.updateIntervalHours,
                allowSameDayCorrection = config.allowSameDayCorrection
            )
        }
    }

    fun setEnabled(enabled: Boolean) { preferences.put(BooleanNonKey.AiProfileEnabled, enabled) }
    fun isEnabled(): Boolean = preferences.get(BooleanNonKey.AiProfileEnabled)

    suspend fun getApiUrl(): String = getConfig().apiUrl
    suspend fun getApiKey(): String = getConfig().apiKey

    fun resetCache() { currentConfig = null }

    private fun parseAggressiveness(): AiAggressiveness {
        return try {
            val value = preferences.get(StringNonKey.AiAggressiveness)
            if (value.isNotBlank()) {
                AiAggressiveness.valueOf(value)
            } else {
                AiAggressiveness.MODERATE
            }
        } catch (e: Exception) {
            AiAggressiveness.MODERATE
        }
    }
    fun getSettingsSummary(): Map<String, String> {
        return linkedMapOf(
            "API URL" to (preferences.get(StringNonKey.AiApiUrl).takeIf { it.isNotBlank() } ?: DEFAULT_API_URL),
            "API Key" to maskApiKey(preferences.get(StringNonKey.AiApiKey)),
            "Aggressiveness" to parseAggressiveness().name.lowercase().replaceFirstChar { it.uppercase() },
            "Analysis period" to "${preferences.get(IntNonKey.AiAnalysisDays)} days",
            "Spike threshold" to "${preferences.get(IntNonKey.AiSpikeThreshold)} mg/dL",
            "Max spikes/day" to preferences.get(IntNonKey.AiMaxSpikesPerDay).toString()
,
            "Check interval" to "${preferences.get(IntNonKey.AiUpdateIntervalHours)} hours",
            "Same-day correction" to if (preferences.get(BooleanNonKey.AiAllowSameDayCorrection)) "Yes" else "No"
        )
    }

    private fun maskApiKey(key: String): String {
        return when {
            key.isBlank() -> "(not set)"
            key.length <= 8 -> "\u2022".repeat(key.length)
            else -> "${key.take(4)}${"\u2022".repeat(key.length - 4)}"
        }
    }

    fun isConfigValid(): Boolean {
        return preferences.get(StringNonKey.AiApiUrl).isNotBlank() &&
               preferences.get(StringNonKey.AiApiKey).isNotBlank()
    }

}

