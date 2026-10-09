package app.gmaaps.database.persistence.converters

import app.gmaaps.core.data.model.EB
import app.gmaaps.database.entities.ExtendedBolus

fun ExtendedBolus.fromDb(): EB =
    EB(
        id = this.id,
        version = this.version,
        dateCreated = this.dateCreated,
        isValid = this.isValid,
        referenceId = this.referenceId,
        timestamp = this.timestamp,
        utcOffset = this.utcOffset,
        duration = this.duration,
        amount = this.amount,
        isEmulatingTempBasal = this.isEmulatingTempBasal,
        ids = this.interfaceIDs.fromDb()
    )

fun EB.toDb(): ExtendedBolus =
    ExtendedBolus(
        id = this.id,
        version = this.version,
        dateCreated = this.dateCreated,
        isValid = this.isValid,
        referenceId = this.referenceId,
        timestamp = this.timestamp,
        utcOffset = this.utcOffset,
        duration = this.duration,
        amount = this.amount,
        isEmulatingTempBasal = this.isEmulatingTempBasal,
        interfaceIDs_backing = this.ids.toDb()
    )

