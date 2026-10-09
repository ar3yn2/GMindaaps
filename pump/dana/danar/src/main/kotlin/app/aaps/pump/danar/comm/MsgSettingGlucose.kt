package app.gmaaps.pump.danar.comm

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.pump.dana.DanaPump

/**
 * Created by mike on 05.07.2016.
 */
class MsgSettingGlucose(
    injector: MetroMemberInjector
) : MessageBase(injector) {

    init {
        setCommand(0x3209)
        aapsLogger.debug(LTag.PUMPCOMM, "New message")
    }

    override fun handleMessage(bytes: ByteArray) {
        danaPump.units = intFromBuff(bytes, 0, 1)
        danaPump.easyBasalMode = intFromBuff(bytes, 1, 1)
        aapsLogger.debug(LTag.PUMPCOMM, "Pump units: " + if (danaPump.units == DanaPump.UNITS_MGDL) "MGDL" else "MMOL")
        aapsLogger.debug(LTag.PUMPCOMM, "Easy basal mode: " + danaPump.easyBasalMode)
    }
}
