package app.gmaaps.plugins.smoothing.keys

import app.gmaaps.core.keys.interfaces.IntNonPreferenceKey

enum class UkfIntNonKey(
    override val key: String,
    override val defaultValue: Int,
) : IntNonPreferenceKey {

    SessionId("ukf_session_id", 0),
}
