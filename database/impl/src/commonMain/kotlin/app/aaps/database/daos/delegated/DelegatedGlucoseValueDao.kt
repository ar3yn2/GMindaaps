package app.gmaaps.database.daos.delegated

import app.gmaaps.database.daos.GlucoseValueDao
import app.gmaaps.database.entities.GlucoseValue
import app.gmaaps.database.entities.interfaces.DBEntry

internal class DelegatedGlucoseValueDao(changes: MutableList<DBEntry>, private val dao: GlucoseValueDao) : DelegatedDao(changes), GlucoseValueDao by dao {

    override suspend fun insertNewEntry(entry: GlucoseValue): Long {
        changes.add(entry)
        return dao.insertNewEntry(entry)
    }

    override suspend fun updateExistingEntry(entry: GlucoseValue): Long {
        changes.add(entry)
        return dao.updateExistingEntry(entry)
    }
}
