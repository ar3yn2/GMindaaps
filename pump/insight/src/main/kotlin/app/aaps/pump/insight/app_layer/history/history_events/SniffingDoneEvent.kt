package app.gmaaps.pump.insight.app_layer.history.history_events

import app.gmaaps.pump.insight.utils.ByteBuf

class SniffingDoneEvent : HistoryEvent() {

    internal var amount = 0.0
        private set

    override fun parse(byteBuf: ByteBuf) {
        amount = byteBuf.readUInt16Decimal()
    }
}
