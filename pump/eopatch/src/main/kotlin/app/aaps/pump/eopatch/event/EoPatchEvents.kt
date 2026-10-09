package app.gmaaps.pump.eopatch.event

import app.gmaaps.core.interfaces.rx.events.Event
import app.gmaaps.pump.eopatch.alarm.AlarmCode

data class EventEoPatchAlarm(val alarmCodes: Set<AlarmCode>, val isFirst: Boolean = false) : Event()
class EventPatchActivationNotComplete : Event()

