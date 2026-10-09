package app.gmaaps.pump.danar.comm

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.logging.LTag

class MsgHistoryNewDone(
    injector: MetroMemberInjector
) : MessageBase(injector) {

    init {
        setCommand(0x42F1)
        danaPump.historyDoneReceived = false
        aapsLogger.debug(LTag.PUMPCOMM, "New message")
    }

    override fun handleMessage(bytes: ByteArray) {
        danaPump.historyDoneReceived = true
        aapsLogger.debug(LTag.PUMPCOMM, "History new done received")
    }
}
