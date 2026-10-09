package app.gmaaps.implementation.androidNotification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import app.gmaaps.core.interfaces.ui.UiInteraction
import app.gmaaps.core.objects.workflow.MetroBroadcastReceiver
import dev.zacsweers.metro.Inject

/**
 * Handles the "Mute" action on the background alarm notification: silences every audible alarm
 * (the looping [app.gmaaps.core.interfaces.notifications.AlarmSoundPlayer] plus the alarm
 * notifications) via [UiInteraction.stopAlarm], without the user having to open the alarm screen.
 * Primary use: the case where `ErrorActivity` could not be brought to the foreground (another app on
 * top) so the user's only lock-screen surface is the notification.
 *
 * A manifest [BroadcastReceiver] is not injected by the system, so it asks the graph itself - the
 * same way the other receivers in this module do.
 */
class AlarmMuteReceiver : MetroBroadcastReceiver() {

    @Inject lateinit var uiInteraction: UiInteraction

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        uiInteraction.stopAlarm("Notification mute action")
    }
}

