package app.aaps.profile.ai

import app.aaps.core.keys.interfaces.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.concurrent.Volatile

class AiConfigStore(
    private val preferences: Preferences
) {
    companion object {
        private const val PREF_AI_ENABLED = "ai_profile_enabled"
        private const val PREF_API_URL = "ai_api_url"
        private const val PREF_API_KEY = "ai_api_key"
        private const val PREF_AGGRESSIVENESS = "ai_aggressiveness"
        private const val PREF_ANALYSIS_DAYS = "ai_analysis_days"
        private const val PREF_SPIKE_THRESHOLD = "ai_spike_threshold"
        private const val PREF_MAX_SPIKES_PER_DAY = "ai_max_spikes_per_day"
        private const val PREF_UPDATE_INTERVAL_HOURS = "ai_update_interval_hours"
        private const val PREF_ALLOW_SAME_DAY_CORRECTION = "ai_allow_same_day_correction"
        private const val DEFAULT_API_URL = "https://api.your-ai-service.com/v1/analyze"
    }

    @Volatile private var currentConfig: AiProfileConfig? = null

    suspend fun getConfig(): AiProfileConfig {
        return withContext(Dispatchers.IO) {
            currentConfig?.let { return@withContext it }
            
            val config = AiProfileConfig(
                aggressiveness = parseAggressiveness(),
                apiUrl = preferences.getString(PREF_API_URL, DEFAULT_API_URL),
                apiKey = preferences.getString(PREF_API_KEY, ""),
                analysisDays = preferences.getInt(PREF_ANALYSIS_DAYS, 7).takeIf { it in setOf(7, 30) } ?: 7,
                spikeThreshold = preferences.getInt(PREF_SPIKE_THRESHOLD, 30).coerceIn(15, 60),
                maxSpikesPerDay = preferences.getInt(PREF_MAX_SPIKES_PER_DAY, 2).coerceIn(1, 5),
                updateIntervalHours = preferences.getInt(PREF_UPDATE_INTERVAL_HOURS, 24).coerceIn(12, 72),
                allowSameDayCorrection = preferences.getBoolean(PREF_ALLOW_SAME_DAY_CORRECTION, false)
            )
            currentConfig = config
            config
        }
    }

    suspend fun updateConfig(config: AiProfileConfig) {
        withContext(Dispatchers.IO) {
            preferences.putString(PREF_API_URL, config.apiUrl)
            preferences.putString(PREF_API_KEY, config.apiKey)
            preferences.putInt(PREF_ANALYSIS_DAYS, config.analysisDays)
            preferences.putInt(PREF_SPIKE_THRESHOLD, config.spikeThreshold)
            preferences.putInt(PREF_MAX_SPIKES_PER_DAY, config.maxSpikesPerDay)
            preferences.putInt(PREF_UPDATE_INTERVAL_HOURS, config.updateIntervalHours)
            preferences.putBoolean(PREF_ALLOW_SAME_DAY_CORRECTION, config.allowSameDayCorrection)
            
            currentConfig = AiProfileConfig(
                aggressiveness = config.aggressiveness,
                apiUrl = config.apiUrl,
                apiKey = config.apiKey,
                analysisDays = config.analysisDays,
                spikeThreshold = config.spikeThreshold,
                maxSpikesPerDay = config.maxSpikesPerDay,
                updateIntervalHours = config.updateIntervalHours,
                allowSameDayCorrection = config.allowSameDayCorrection
            )
        }
    }

    fun setEnabled(enabled: Boolean) { preferences.putBoolean(PREF_AI_ENABLED, enabled) }
    fun isEnabled(): Boolean = preferences.getBoolean(PREF_AI_ENABLED, false)
    
    suspend fun getApiUrl(): String = getConfig().apiUrl
    suspend fun getApiKey(): String = getConfig().apiKey
    
    fun resetCache() { currentConfig = null }

    private fun parseAggressiveness(): AiAggressiveness {
        return try {
            val value = preferences.getString(PREF_AGGRESSIVENESS, "MODERATE")
            AiAggressiveness.valueOf(value.uppercase())
        } catch (e: Exception) {
            AiAggressiveness.MODERATE
        }
    }

    fun getSettingsSummary(): Map<String, String> {
        return mapOf(
            "API URL" to preferences.getString(PREF_API_URL, DEFAULT_API_URL),
            "API Key" to maskApiKey(preferences.getString(PREF_API_KEY, "")),
            "Aggressiveness" -> parseAggressiveness().name.lowercase().replaceFirstChar { it.uppercase() },
            "Analysis period" to "${preferences.getInt(PREF_ANALYSIS_DAYS, 7)} days",
            "Spike threshold" to "${preferences.getInt(PREF_SPIKE_THRESHOLD, 30)} mg/dL",
            "Max spikes/day" to preferences.getInt(PREF_MAX_SPIKES_PER_DAY, 2).toString(),
            "Check interval" to "${preferences.getInt(PREF_UPDATE_INTERVAL_HOURS, 24)} hours",
            "Same-day correction" to if (preferences.getBoolean(PREF_ALLOW_SAME_DAY_CORRECTION, false)) "Yes" else "No"
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
        return preferences.getString(PREF_API_URL, "").isNotBlank() &&
               preferences.getString(PREF_API_KEY, "").isNotBlank()
    }
}

