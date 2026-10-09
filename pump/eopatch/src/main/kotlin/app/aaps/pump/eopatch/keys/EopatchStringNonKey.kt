package app.gmaaps.pump.eopatch.keys

import app.gmaaps.core.keys.interfaces.StringNonPreferenceKey

enum class EopatchStringNonKey(
    override val key: String,
    override val defaultValue: String,
) : StringNonPreferenceKey {

    Alarms("eopatch_alarms", ""),
    BolusCurrent("eopatch_bolus_current", ""),
    PatchState("eopatch_patch_state", ""),
    PatchConfig("eopatch_patch_config", ""),
    NormalBasal("eopatch_normal_basal", ""),
    TempBasal("eopatch_temp_basal", ""),
}

