package app.gmaaps.pump.diaconn.packet

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.pump.diaconn.DiaconnG8Pump
import dev.zacsweers.metro.Inject

/**
 * InjectionCancelSettingPacket
 */
@Suppress("SpellCheckingInspection")
class InjectionCancelSettingPacket(
    injector: MetroMemberInjector,
    private var reqMsgType: Byte, // 명령코드
) : DiaconnG8Packet(injector) {

    @Inject lateinit var diaconnG8Pump: DiaconnG8Pump

    init {
        msgType = 0x2B
        aapsLogger.debug(LTag.PUMPCOMM, "InjectionCancelSettingPacket INIT")
    }

    override fun encode(msgSeq: Int): ByteArray {
        val buffer = prefixEncode(msgType, msgSeq, MSG_CON_END)
        buffer.put(reqMsgType)
        return suffixEncode(buffer)
    }

    override val friendlyName = "PUMP_INJECTION_CANCEL_SETTING_REQUEST"
}
