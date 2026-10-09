package app.gmaaps.pump.insight.app_layer.history

import app.gmaaps.pump.insight.app_layer.AppLayerMessage
import app.gmaaps.pump.insight.app_layer.Service
import app.gmaaps.pump.insight.descriptors.MessagePriority

class StopReadingHistoryMessage : AppLayerMessage(MessagePriority.NORMAL, false, false, Service.HISTORY)
