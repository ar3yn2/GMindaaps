package app.gmaaps.pump.omnipod.common.keys

import app.gmaaps.core.keys.interfaces.StringNonPreferenceKey

enum class DashStringNonPreferenceKey(
    override val key: String,
    override val defaultValue: String,
) : StringNonPreferenceKey {

    PodState("AAPS.OmnipodDash.pod_state", ""),
}

