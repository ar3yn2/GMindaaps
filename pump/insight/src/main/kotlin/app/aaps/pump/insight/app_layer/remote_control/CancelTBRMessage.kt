package app.gmaaps.pump.insight.app_layer.remote_control

import app.gmaaps.pump.insight.app_layer.AppLayerMessage
import app.gmaaps.pump.insight.app_layer.Service
import app.gmaaps.pump.insight.descriptors.MessagePriority

class CancelTBRMessage : AppLayerMessage(MessagePriority.HIGHER, false, false, Service.REMOTE_CONTROL)
