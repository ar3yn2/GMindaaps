package app.gmaaps.database.persistence.converters

import app.gmaaps.core.data.model.GV
import app.gmaaps.database.entities.GlucoseValue

fun GlucoseValue.fromDb(): GV =
    GV(
        id = this.id,
        version = this.version,
        dateCreated = this.dateCreated,
        isValid = this.isValid,
        referenceId = this.referenceId,
        timestamp = this.timestamp,
        utcOffset = this.utcOffset,
        raw = this.raw,
        value = this.value,
        trendArrow = this.trendArrow.fromDb(),
        noise = this.noise,
        sourceSensor = this.sourceSensor.fromDb(),
        ids = this.interfaceIDs.fromDb()
    )

fun GV.toDb(): GlucoseValue =
    GlucoseValue(
        id = this.id,
        version = this.version,
        dateCreated = this.dateCreated,
        isValid = this.isValid,
        referenceId = this.referenceId,
        timestamp = this.timestamp,
        utcOffset = this.utcOffset,
        raw = this.raw,
        value = this.value,
        trendArrow = this.trendArrow.toDb(),
        noise = this.noise,
        sourceSensor = this.sourceSensor.toDb(),
        interfaceIDs_backing = this.ids.toDb()
    )
