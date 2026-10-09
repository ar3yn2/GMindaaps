package app.gmaaps.pump.insight.satl

import app.gmaaps.pump.insight.utils.ByteBuf

class VerifyConfirmResponse : SatlMessage() {

    lateinit var pairingStatus: PairingStatus
        private set

    override fun parse(byteBuf: ByteBuf) {
        pairingStatus = PairingStatus.fromId(byteBuf.readUInt16LE())
    }
}
