package app.gmaaps.plugins.sync.extensions

import app.gmaaps.core.data.model.IDs

fun IDs.contentEqualsTo(other: IDs): Boolean =
    nightscoutId == other.nightscoutId &&
        nightscoutSystemId == other.nightscoutSystemId &&
        pumpType == other.pumpType &&
        pumpSerial == other.pumpSerial &&
        temporaryId == other.temporaryId &&
        pumpId == other.pumpId &&
        startId == other.startId &&
        endId == other.endId


