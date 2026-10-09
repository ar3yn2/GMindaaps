package app.gmaaps.pump.danar.comm

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.logging.LTag

class MsgSettingActiveProfile(
    injector: MetroMemberInjector
) : MessageBase(injector) {

    init {
        setCommand(0x320C)
        aapsLogger.debug(LTag.PUMPCOMM, "New message")
    }

    override fun handleMessage(bytes: ByteArray) {
        danaPump.activeProfile = intFromBuff(bytes, 0, 1)
        aapsLogger.debug(LTag.PUMPCOMM, "Active profile number: " + danaPump.activeProfile)
    }

}
