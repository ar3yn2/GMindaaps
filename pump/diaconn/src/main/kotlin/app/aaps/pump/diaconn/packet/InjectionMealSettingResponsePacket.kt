package app.gmaaps.pump.diaconn.packet

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.pump.diaconn.DiaconnG8Pump
import dev.zacsweers.metro.Inject

/**
 * InjectionMealSettingResponsePacket
 */
class InjectionMealSettingResponsePacket(
    injector: MetroMemberInjector
) : DiaconnG8Packet(injector) {

    @Inject lateinit var diaconnG8Pump: DiaconnG8Pump
    var result = 0

    init {
        msgType = 0x86.toByte()
        aapsLogger.debug(LTag.PUMPCOMM, "InjectionMealSettingResponsePacket init ")
    }

    override fun handleMessage(data: ByteArray) {
        val defectCheck = defect(data)
        if (defectCheck != 0) {
            aapsLogger.debug(LTag.PUMPCOMM, "InjectionMealSettingResponsePacket Got some Error")
            failed = true
            return
        } else failed = false

        val bufferData = prefixDecode(data)
        result = getByteToInt(bufferData)

        if (!isSuccSettingResponseResult(result)) {
            diaconnG8Pump.resultErrorCode = result
            failed = true
            return
        }
        diaconnG8Pump.otpNumber = getIntToInt(bufferData)
        aapsLogger.debug(LTag.PUMPCOMM, "Result --> $result")
        aapsLogger.debug(LTag.PUMPCOMM, "otpNumber --> ${diaconnG8Pump.otpNumber}")
    }

    override val friendlyName = "PUMP_INJECTION_MEAL_SETTING_RESPONSE"
}
