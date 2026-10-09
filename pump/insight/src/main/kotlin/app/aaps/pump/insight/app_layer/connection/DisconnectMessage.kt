package app.gmaaps.pump.insight.app_layer.connection

import app.gmaaps.pump.insight.app_layer.AppLayerMessage
import app.gmaaps.pump.insight.app_layer.Service
import app.gmaaps.pump.insight.descriptors.MessagePriority
import app.gmaaps.pump.insight.utils.ByteBuf
import org.spongycastle.util.encoders.Hex

class DisconnectMessage : AppLayerMessage(MessagePriority.NORMAL, false, false, Service.CONNECTION) {

    override val data: ByteBuf
        get() = ByteBuf.from(Hex.decode("0360"))
}
