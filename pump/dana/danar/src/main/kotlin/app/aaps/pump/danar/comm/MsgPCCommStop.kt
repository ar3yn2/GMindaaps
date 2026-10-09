package app.gmaaps.pump.danar.comm

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.logging.LTag

class MsgPCCommStop(
    injector: MetroMemberInjector
) : MessageBase(injector) {

    init {
        setCommand(0x3002)
        aapsLogger.debug(LTag.PUMPCOMM, "New message")
    }

    override fun handleMessage(bytes: ByteArray) {
        aapsLogger.debug(LTag.PUMPCOMM, "PC comm stop received")
    }
}
