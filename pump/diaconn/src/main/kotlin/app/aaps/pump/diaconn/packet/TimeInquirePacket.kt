package app.gmaaps.pump.diaconn.packet

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.pump.diaconn.DiaconnG8Pump
import dev.zacsweers.metro.Inject

/**
 * TimeInquirePacket
 */
class TimeInquirePacket(
    injector: MetroMemberInjector
) : DiaconnG8Packet(injector) {

    @Inject lateinit var diaconnG8Pump: DiaconnG8Pump

    init {
        msgType = 0x4F.toByte()
        aapsLogger.debug(LTag.PUMPCOMM, "TimeInquirePacket init")
    }

    override fun encode(msgSeq: Int): ByteArray {
        val buffer = prefixEncode(msgType, msgSeq, MSG_CON_END)
        return suffixEncode(buffer)
    }

    override val friendlyName = "PUMP_TIME_REQUEST"
}
