package app.gmaaps.core.keys

import app.gmaaps.core.keys.interfaces.IntNonPreferenceKey

@Suppress("SpellCheckingInspection")
enum class IntNonKey(
    override val key: String,
    override val defaultValue: Int,
) : IntNonPreferenceKey {

    ObjectivesManualEnacts("ObjectivesmanualEnacts", 0),
    TddCycleOffset("tdd_cycle_offset", 0),
    // AI Profile settings
    AiAnalysisDays("ai_analysis_days", 7),
    AiSpikeThreshold("ai_spike_threshold", 30),
    AiMaxSpikesPerDay("ai_max_spikes_per_day", 2),
    AiUpdateIntervalHours("ai_update_interval_hours", 24),
    AiMinCoveragePercent("ai_min_coverage_percent", 80),
    AiSpikeLookbackMinutes("ai_spike_lookback_minutes", 90),
    AiMinDataDays("ai_min_data_days", 3)

}
