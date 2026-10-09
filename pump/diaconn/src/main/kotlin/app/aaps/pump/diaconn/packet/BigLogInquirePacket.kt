package app.gmaaps.pump.diaconn.packet

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.pump.diaconn.DiaconnG8Pump
import dev.zacsweers.metro.Inject

/**

 * BigLogInquirePacket
 */
class BigLogInquirePacket(
    injector: MetroMemberInjector,
    private val start: Int,
    private val end: Int,
    private val delay: Int
) : DiaconnG8Packet(injector) {

    @Inject lateinit var diaconnG8Pump: DiaconnG8Pump

    init {
        msgType = 0x72
        aapsLogger.debug(LTag.PUMPCOMM, "BigLogInquirePacket init")
    }

    override fun encode(msgSeq: Int): ByteArray {
        val buffer = prefixEncode(msgType, msgSeq, MSG_CON_END)
        buffer.putShort(start.toShort())
        buffer.putShort(end.toShort())
        buffer.put(delay.toByte())
        return suffixEncode(buffer)
    }

    override val friendlyName = "PUMP_BIG_LOG_INQUIRE"
}
