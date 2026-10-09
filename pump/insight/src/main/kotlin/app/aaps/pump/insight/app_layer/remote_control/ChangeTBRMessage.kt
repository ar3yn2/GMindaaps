package app.gmaaps.pump.insight.app_layer.remote_control

import app.gmaaps.pump.insight.app_layer.AppLayerMessage
import app.gmaaps.pump.insight.app_layer.Service
import app.gmaaps.pump.insight.descriptors.MessagePriority
import app.gmaaps.pump.insight.utils.ByteBuf

class ChangeTBRMessage : AppLayerMessage(MessagePriority.NORMAL, false, true, Service.REMOTE_CONTROL) {

    internal var percentage = 0
    internal var duration = 0
    override val data: ByteBuf
        get() {
            val byteBuf = ByteBuf(6).apply {
                putUInt16LE(percentage)
                putUInt16LE(duration)
                putUInt16LE(31)
            }
            return byteBuf
        }

}
