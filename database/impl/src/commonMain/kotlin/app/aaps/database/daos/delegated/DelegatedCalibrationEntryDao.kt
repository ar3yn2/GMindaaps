package app.gmaaps.database.daos.delegated

import app.gmaaps.database.daos.CalibrationEntryDao
import app.gmaaps.database.entities.CalibrationEntry
import app.gmaaps.database.entities.interfaces.DBEntry

internal class DelegatedCalibrationEntryDao(changes: MutableList<DBEntry>, private val dao: CalibrationEntryDao) : DelegatedDao(changes), CalibrationEntryDao by dao {

    override suspend fun insertNewEntry(entry: CalibrationEntry): Long {
        changes.add(entry)
        return dao.insertNewEntry(entry)
    }

    override suspend fun updateExistingEntry(entry: CalibrationEntry): Long {
        changes.add(entry)
        return dao.updateExistingEntry(entry)
    }
}

