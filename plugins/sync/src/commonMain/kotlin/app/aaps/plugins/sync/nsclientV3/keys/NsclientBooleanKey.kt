package app.gmaaps.plugins.sync.nsclientV3.keys

import app.gmaaps.core.keys.interfaces.BooleanNonPreferenceKey

enum class NsclientBooleanKey(
    override val key: String,
    override val defaultValue: Boolean,
) : BooleanNonPreferenceKey {

    NsPaused("ns_client_paused", false)
}
