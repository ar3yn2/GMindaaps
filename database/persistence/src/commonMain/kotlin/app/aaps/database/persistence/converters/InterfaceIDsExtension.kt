package app.gmaaps.database.persistence.converters

import app.gmaaps.core.data.model.IDs
import app.gmaaps.database.entities.embedments.InterfaceIDs

fun InterfaceIDs.fromDb(): IDs =
    IDs(
        nightscoutSystemId = this.nightscoutSystemId,
        nightscoutId = this.nightscoutId,
        pumpType = this.pumpType?.fromDb(),
        pumpSerial = this.pumpSerial,
        temporaryId = this.temporaryId,
        pumpId = this.pumpId,
        startId = this.startId,
        endId = this.endId
    )

fun IDs.toDb(): InterfaceIDs =
    InterfaceIDs(
        nightscoutSystemId = this.nightscoutSystemId,
        nightscoutId = this.nightscoutId,
        pumpType = this.pumpType?.toDb(),
        pumpSerial = this.pumpSerial,
        temporaryId = this.temporaryId,
        pumpId = this.pumpId,
        startId = this.startId,
        endId = this.endId
    )


