package app.gmaaps.pump.dana.events

import app.gmaaps.core.interfaces.rx.events.Event

/**
 * Fired to update the Dana-RS sync status.
 *
 * @param message The sync status message.
 */
data class EventDanaRSyncStatus(var message: String) : Event()
