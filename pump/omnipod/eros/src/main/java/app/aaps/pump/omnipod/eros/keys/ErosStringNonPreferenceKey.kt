package app.gmaaps.pump.omnipod.eros.keys

import app.gmaaps.core.keys.interfaces.StringNonPreferenceKey

enum class ErosStringNonPreferenceKey(
    override val key: String,
    override val defaultValue: String,
) : StringNonPreferenceKey {

    PodState("AAPS.Omnipod.pod_state", ""),
    ActiveBolus("AAPS.Omnipod.current_bolus", ""),
}
