package app.gmaaps.pump.diaconn.packet

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.pump.diaconn.DiaconnG8Pump
import dev.zacsweers.metro.Inject

/**
 * InjectionSnackSettingPacket
 */
class InjectionSnackSettingPacket(
    injector: MetroMemberInjector,
    private val amount: Int
) : DiaconnG8Packet(injector) {

    @Inject lateinit var diaconnG8Pump: DiaconnG8Pump

    init {
        msgType = 0x07.toByte()
        aapsLogger.debug(LTag.PUMPCOMM, "InjectionSnackSettingPacket init ")
    }

    override fun encode(msgSeq: Int): ByteArray {
        val buffer = prefixEncode(msgType, msgSeq, MSG_CON_END)
        buffer.putShort(amount.toShort())
        return suffixEncode(buffer)
    }

    override val friendlyName = "PUMP_INJECTION_SNACK_SETTING"
}
