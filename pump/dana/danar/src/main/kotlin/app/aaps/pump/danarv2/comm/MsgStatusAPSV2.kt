package app.gmaaps.pump.danarv2.comm

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.pump.danar.comm.MessageBase

class MsgStatusAPSV2(
    injector: MetroMemberInjector
) : MessageBase(injector) {

    init {
        setCommand(0xE001)
        aapsLogger.debug(LTag.PUMPCOMM, "New message")
    }

    override fun handleMessage(bytes: ByteArray) {
        val iob = intFromBuff(bytes, 0, 2) / 100.0
        val deliveredSoFar = intFromBuff(bytes, 2, 2) / 100.0
        danaPump.iob = iob
        aapsLogger.debug(LTag.PUMPCOMM, "Delivered so far: $deliveredSoFar")
        aapsLogger.debug(LTag.PUMPCOMM, "Current pump IOB: $iob")
    }
}
