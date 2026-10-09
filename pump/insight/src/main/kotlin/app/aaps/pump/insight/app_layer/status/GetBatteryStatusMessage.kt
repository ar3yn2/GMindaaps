package app.gmaaps.pump.insight.app_layer.status

import app.gmaaps.pump.insight.app_layer.AppLayerMessage
import app.gmaaps.pump.insight.app_layer.Service
import app.gmaaps.pump.insight.descriptors.BatteryStatus
import app.gmaaps.pump.insight.descriptors.BatteryType
import app.gmaaps.pump.insight.descriptors.MessagePriority
import app.gmaaps.pump.insight.descriptors.SymbolStatus
import app.gmaaps.pump.insight.utils.ByteBuf

class GetBatteryStatusMessage : AppLayerMessage(MessagePriority.NORMAL, false, false, Service.STATUS) {

    internal var batteryStatus: BatteryStatus? = null
        private set

    override fun parse(byteBuf: ByteBuf) {
        batteryStatus = BatteryStatus().apply {
            byteBuf.let {
                batteryType = BatteryType.fromId(it.readUInt16LE())
                batteryAmount = it.readUInt16LE()
                symbolStatus = SymbolStatus.fromId(it.readUInt16LE())
            }
        }
    }
}
