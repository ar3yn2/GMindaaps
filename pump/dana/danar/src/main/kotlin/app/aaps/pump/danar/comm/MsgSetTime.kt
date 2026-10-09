package app.gmaaps.pump.danar.comm

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.logging.LTag

class MsgSetTime(
    injector: MetroMemberInjector,
    time: Long
) : MessageBase(injector) {

    init {
        setCommand(0x330a)
        addParamDateTimeReversed(time)
        aapsLogger.debug(LTag.PUMPCOMM, "New message: time:" + dateUtil.dateAndTimeString(time))
    }

    override fun handleMessage(bytes: ByteArray) {
        val result = intFromBuff(bytes, 0, 1)
        failed = result != 1
        aapsLogger.debug(LTag.PUMPCOMM, "Result of setting time: $result")
    }
}
