package app.gmaaps.database.persistence.converters

import app.gmaaps.core.data.model.CAL
import app.gmaaps.database.entities.CalibrationEntry

fun CalibrationEntry.fromDb(): CAL =
    CAL(
        id = this.id,
        version = this.version,
        dateCreated = this.dateCreated,
        isValid = this.isValid,
        referenceId = this.referenceId,
        timestamp = this.timestamp,
        utcOffset = this.utcOffset,
        fingerstickMgdl = this.fingerstickMgdl,
        sensorMgdlAtPairing = this.sensorMgdlAtPairing,
        ids = this.interfaceIDs.fromDb()
    )

fun CAL.toDb(): CalibrationEntry =
    CalibrationEntry(
        id = this.id,
        version = this.version,
        dateCreated = this.dateCreated,
        isValid = this.isValid,
        referenceId = this.referenceId,
        timestamp = this.timestamp,
        utcOffset = this.utcOffset,
        fingerstickMgdl = this.fingerstickMgdl,
        sensorMgdlAtPairing = this.sensorMgdlAtPairing,
        interfaceIDs_backing = this.ids.toDb()
    )

