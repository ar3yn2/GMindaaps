package app.gmaaps.pump.common.sync

import app.gmaaps.core.data.pump.defs.PumpType

interface PumpSyncEntriesCreator {

    fun generateTempId(objectA: Any): Long
    fun model(): PumpType
    fun serialNumber(): String
}
