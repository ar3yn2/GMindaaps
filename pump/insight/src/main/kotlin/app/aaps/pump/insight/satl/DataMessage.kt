package app.gmaaps.pump.insight.satl

import app.gmaaps.pump.insight.utils.ByteBuf

class DataMessage : SatlMessage() {

    public override lateinit var data: ByteBuf

    override fun parse(byteBuf: ByteBuf) {
        data = byteBuf
    }
}
