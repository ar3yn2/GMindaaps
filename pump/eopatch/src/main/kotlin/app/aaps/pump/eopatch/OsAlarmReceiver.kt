package app.gmaaps.pump.eopatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import app.gmaaps.pump.eopatch.alarm.AlarmCode
import app.gmaaps.pump.eopatch.alarm.AlarmCode.Companion.fromIntent
import app.gmaaps.pump.eopatch.event.EventEoPatchAlarm

/**
 * A plain [BroadcastReceiver]: it has no injected fields, so it needs no injector.
 */
class OsAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        fromIntent(intent)?.let { alarmCode ->
            EoPatchRxBus.publish(EventEoPatchAlarm(HashSet<AlarmCode>().apply { add(alarmCode) }))
        }
    }
}

