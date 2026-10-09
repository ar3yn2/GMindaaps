package app.gmaaps.pump.insight.app_layer.status

import app.gmaaps.pump.insight.app_layer.AppLayerMessage
import app.gmaaps.pump.insight.app_layer.Service
import app.gmaaps.pump.insight.descriptors.CartridgeStatus
import app.gmaaps.pump.insight.descriptors.CartridgeType
import app.gmaaps.pump.insight.descriptors.MessagePriority
import app.gmaaps.pump.insight.descriptors.SymbolStatus
import app.gmaaps.pump.insight.utils.ByteBuf

class GetCartridgeStatusMessage : AppLayerMessage(MessagePriority.NORMAL, false, false, Service.STATUS) {

    internal var cartridgeStatus: CartridgeStatus? = null
        private set

    override fun parse(byteBuf: ByteBuf) {
        cartridgeStatus = CartridgeStatus().apply {
            byteBuf.let {
                isInserted = it.readBoolean()
                cartridgeType = CartridgeType.fromId(it.readUInt16LE())
                symbolStatus = SymbolStatus.fromId(it.readUInt16LE())
                remainingAmount = it.readUInt16Decimal()
            }
        }
    }
}
