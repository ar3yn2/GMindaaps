package app.gmaaps.database.entities.interfaces

interface DBEntryWithDuration {

    var duration: Long

    val durationUnknown get() = duration == Long.MAX_VALUE
}
