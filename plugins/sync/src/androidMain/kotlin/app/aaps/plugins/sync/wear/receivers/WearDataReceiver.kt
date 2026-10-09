package app.gmaaps.plugins.sync.wear.receivers

import android.content.Context
import android.content.Intent
import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.receivers.Intents
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.interfaces.rx.events.EventMobileToWear
import app.gmaaps.core.interfaces.rx.weardata.EventData
import app.gmaaps.core.objects.workflow.MetroBroadcastReceiver
import app.gmaaps.core.utils.receivers.BundleLogger
import dev.zacsweers.metro.HasMemberInjections
import dev.zacsweers.metro.Inject

// The annotation is Metro's requirement for a non-final class with injected fields. Nothing subclasses
// this today, but if something does it needs its own map entry - the lookup uses the runtime class.
@HasMemberInjections
open class WearDataReceiver : MetroBroadcastReceiver() {

    @Inject lateinit var aapsLogger: AAPSLogger
    @Inject lateinit var config: Config
    @Inject lateinit var rxBus: RxBus

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val bundle = intent.extras ?: return
        aapsLogger.debug(LTag.WEAR, "onReceive ${intent.action} ${BundleLogger.log(bundle)}")

        when (intent.action) {
            Intents.AAPS_CLIENT_WEAR_DATA -> {
                val client = bundle.getInt(CLIENT)
                val data = bundle.getString(DATA)
                if (client == 0 || data == null) {
                    aapsLogger.error(LTag.WEAR, "Misformatted data received. Ignoring.")
                    return
                }
                // Check for allowed configuration
                if (
                    config.AAPSCLIENT1 && (client == 2 || client == 3) ||
                    (config.APS || config.PUMPCONTROL) && (client == 1 || client == 2 || client == 3)
                ) {
                    // Send to phone
                    val eventData = EventData.deserialize(data)
                    if (eventData is EventData.EventDataSet)
                        rxBus.send(EventMobileToWear(eventData))
                }
            }
        }
    }

    companion object {

        const val PERMISSION = "app.gmaaps.weardata.permission"
        const val CLIENT = "client"
        const val DATA = "data"
    }
}

