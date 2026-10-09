package app.gmaaps.core.keys

import app.gmaaps.core.keys.interfaces.DoubleNonPreferenceKey

enum class DoubleNonKey(
    override val key: String,
    override val defaultValue: Double,
) : DoubleNonPreferenceKey {

}
