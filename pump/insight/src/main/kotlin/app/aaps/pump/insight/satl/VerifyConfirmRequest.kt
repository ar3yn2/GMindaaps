package app.gmaaps.pump.insight.satl

import app.gmaaps.pump.insight.utils.ByteBuf

class VerifyConfirmRequest : SatlMessage() {

    override val data: ByteBuf
        get() {
            val byteBuf = ByteBuf(2)
            byteBuf.putUInt16LE(PairingStatus.CONFIRMED.id)
            return byteBuf
        }
}
