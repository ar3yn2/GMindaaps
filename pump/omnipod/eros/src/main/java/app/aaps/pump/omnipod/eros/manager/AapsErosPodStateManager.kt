package app.gmaaps.pump.omnipod.eros.manager

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.notifications.NotificationId
import app.gmaaps.core.interfaces.notifications.NotificationManager
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.pump.omnipod.eros.driver.manager.ErosPodStateManager
import app.gmaaps.pump.omnipod.eros.event.EventOmnipodErosActiveAlertsChanged
import app.gmaaps.pump.omnipod.eros.event.EventOmnipodErosFaultEventChanged
import app.gmaaps.pump.omnipod.eros.event.EventOmnipodErosTbrChanged
import app.gmaaps.pump.omnipod.eros.event.EventOmnipodErosUncertainTbrRecovered
import app.gmaaps.pump.omnipod.eros.keys.ErosStringNonPreferenceKey
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

/**
 * Was Java.
 */
@SingleIn(AppScope::class)
@Inject
class AapsErosPodStateManager(
    aapsLogger: AAPSLogger,
    private val preferences: Preferences,
    private val rxBus: RxBus,
    private val notificationManager: NotificationManager
) : ErosPodStateManager(aapsLogger) {

    override fun readPodState(): String = preferences.get(ErosStringNonPreferenceKey.PodState)

    override fun storePodState(podState: String) {
        preferences.put(ErosStringNonPreferenceKey.PodState, podState)
    }

    override fun onUncertainTbrRecovered() {
        rxBus.send(EventOmnipodErosUncertainTbrRecovered())
    }

    override fun onTbrChanged() {
        rxBus.send(EventOmnipodErosTbrChanged())
    }

    override fun onActiveAlertsChanged() {
        rxBus.send(EventOmnipodErosActiveAlertsChanged())
    }

    override fun onFaultEventChanged() {
        rxBus.send(EventOmnipodErosFaultEventChanged())
    }

    override fun onUpdatedFromResponse() {
        notificationManager.dismiss(NotificationId.OMNIPOD_STARTUP_STATUS_REFRESH_FAILED)
    }
}

