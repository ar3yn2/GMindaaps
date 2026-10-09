package app.gmaaps.core.data.model

import app.gmaaps.core.data.time.systemUtcOffsetAt
import app.gmaaps.core.data.ue.Action
import app.gmaaps.core.data.ue.Sources
import app.gmaaps.core.data.ue.ValueWithUnit

data class UE(
    var id: Long = 0L,
    override var timestamp: Long,
    var utcOffset: Long = systemUtcOffsetAt(timestamp),
    var action: Action,
    var source: Sources,
    var note: String,
    var values: List<ValueWithUnit>
) : TimeStamped
