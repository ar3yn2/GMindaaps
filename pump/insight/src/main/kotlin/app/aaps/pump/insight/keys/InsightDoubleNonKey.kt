package app.gmaaps.pump.insight.keys

import app.gmaaps.core.keys.interfaces.DoubleNonPreferenceKey

enum class InsightDoubleNonKey(
    override val key: String,
    override val defaultValue: Double,
) : DoubleNonPreferenceKey {

    LastBolusAmount("insight_last_bolus_amount", 0.0),
}


