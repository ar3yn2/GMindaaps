package app.gmaaps.plugins.smoothing.keys

import app.gmaaps.core.keys.interfaces.DoubleNonPreferenceKey

enum class UkfDoubleNonKey(
    override val key: String,
    override val defaultValue: Double,
) : DoubleNonPreferenceKey {

    LearnedR("ukf_learned_r", 25.0), //rInit
}
