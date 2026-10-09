package app.gmaaps.core.objects.extensions

import app.gmaaps.core.data.iob.InMemoryGlucoseValue
import app.gmaaps.core.data.model.GV

fun InMemoryGlucoseValue.Companion.fromGv(gv: GV): InMemoryGlucoseValue =
    InMemoryGlucoseValue(timestamp = gv.timestamp, value = gv.value, trendArrow = gv.trendArrow, sourceSensor = gv.sourceSensor)


