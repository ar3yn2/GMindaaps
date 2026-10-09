package app.gmaaps.pump.common.hw.rileylink.keys

import app.gmaaps.core.keys.interfaces.DoubleNonPreferenceKey

enum class RileyLinkDoubleKey(
    override val key: String,
    override val defaultValue: Double,
) : DoubleNonPreferenceKey {

    LastGoodDeviceFrequency("AAPS.RileyLink.LastGoodDeviceFrequency", 0.0),
}
