package app.gmaaps.pump.common.hw.rileylink.keys

import app.gmaaps.core.keys.interfaces.LongNonPreferenceKey

enum class RileyLinkLongKey(
    override val key: String,
    override val defaultValue: Long,
) : LongNonPreferenceKey {

    LastGoodDeviceCommunicationTime("AAPS.RileyLink.lastGoodDeviceCommunicationTime", 0L),
}
