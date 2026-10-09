package app.gmaaps.pump.diaconn.packet

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.pump.diaconn.DiaconnG8Pump
import dev.zacsweers.metro.Inject

/**
 * InjectionBasalSettingPacket
 */
class InjectionBasalSettingPacket(
    injector: MetroMemberInjector,
    private val pattern: Int
) : DiaconnG8Packet(injector) {

    @Inject lateinit var diaconnG8Pump: DiaconnG8Pump

    init {
        msgType = 0x0C.toByte()
        aapsLogger.debug(LTag.PUMPCOMM, "InjectionBasalSettingPacket init ")
    }

    override fun encode(msgSeq: Int): ByteArray {
        val buffer = prefixEncode(msgType, msgSeq, MSG_CON_END)
        buffer.put(pattern.toByte())
        return suffixEncode(buffer)
    }

    override val friendlyName = "PUMP_INJECTION_BASAL_SETTING"
}
