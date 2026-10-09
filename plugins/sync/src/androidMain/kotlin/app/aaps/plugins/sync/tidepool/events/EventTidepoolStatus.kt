package app.gmaaps.plugins.sync.tidepool.events

import app.gmaaps.core.interfaces.rx.events.Event

data class EventTidepoolStatus(val status: String) : Event() {

    var date: Long = System.currentTimeMillis()
}
