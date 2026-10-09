package app.gmaaps.pump.diaconn.packet

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.pump.diaconn.DiaconnG8Pump
import dev.zacsweers.metro.Inject

/**
 * InjectionSnackInquirePacket
 */
class InjectionSnackInquirePacket(injector: MetroMemberInjector) : DiaconnG8Packet(injector) {

    @Inject lateinit var diaconnG8Pump: DiaconnG8Pump

    init {
        msgType = 0x47.toByte()
        aapsLogger.debug(LTag.PUMPCOMM, "InjectionSnackInquirePacket init ")
    }

    override fun encode(msgSeq: Int): ByteArray {
        return suffixEncode(prefixEncode(msgType, msgSeq, MSG_CON_END))
    }

    override val friendlyName = "PUMP_INJECTION_SNACK_INQUIRE"
}
