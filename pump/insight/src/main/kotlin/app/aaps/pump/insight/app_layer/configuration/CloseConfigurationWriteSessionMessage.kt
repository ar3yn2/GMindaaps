package app.gmaaps.pump.insight.app_layer.configuration

import app.gmaaps.pump.insight.app_layer.AppLayerMessage
import app.gmaaps.pump.insight.app_layer.Service
import app.gmaaps.pump.insight.descriptors.MessagePriority

class CloseConfigurationWriteSessionMessage : AppLayerMessage(MessagePriority.NORMAL, false, false, Service.CONFIGURATION)
