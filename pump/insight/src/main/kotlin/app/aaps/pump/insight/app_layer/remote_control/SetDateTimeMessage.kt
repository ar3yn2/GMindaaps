package app.gmaaps.pump.insight.app_layer.remote_control

import app.gmaaps.pump.insight.app_layer.AppLayerMessage
import app.gmaaps.pump.insight.app_layer.Service
import app.gmaaps.pump.insight.descriptors.MessagePriority
import app.gmaaps.pump.insight.descriptors.PumpTime
import app.gmaaps.pump.insight.utils.ByteBuf

class SetDateTimeMessage : AppLayerMessage(MessagePriority.NORMAL, false, true, Service.CONFIGURATION) {

    internal lateinit var pumpTime: PumpTime
    override val data: ByteBuf
        get() {
            val byteBuf = ByteBuf(7).apply {
                putUInt16LE(pumpTime.year)
                putUInt8(pumpTime.month.toShort())
                putUInt8(pumpTime.day.toShort())
                putUInt8(pumpTime.hour.toShort())
                putUInt8(pumpTime.minute.toShort())
                putUInt8(pumpTime.second.toShort())
            }
            return byteBuf
        }
}
