package app.gmaaps.pump.diaconn.packet

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.pump.diaconn.DiaconnG8Pump
import dev.zacsweers.metro.Inject

/**
 * SnackLimitInquirePacket
 */
class SnackLimitInquirePacket(
    injector: MetroMemberInjector
) : DiaconnG8Packet(injector) {

    @Inject lateinit var diaconnG8Pump: DiaconnG8Pump

    init {
        msgType = 0x50.toByte()
        aapsLogger.debug(LTag.PUMPCOMM, "SnackLimitInquirePacket limit request")
    }

    override fun encode(msgSeq: Int): ByteArray {
        val buffer = prefixEncode(msgType, msgSeq, MSG_CON_END)
        return suffixEncode(buffer)
    }

    override val friendlyName = "PUMP_SNACK_LIMIT_REQUEST"
}
