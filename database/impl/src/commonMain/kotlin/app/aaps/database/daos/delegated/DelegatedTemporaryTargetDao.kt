package app.gmaaps.database.daos.delegated

import app.gmaaps.database.daos.TemporaryTargetDao
import app.gmaaps.database.entities.TemporaryTarget
import app.gmaaps.database.entities.interfaces.DBEntry

internal class DelegatedTemporaryTargetDao(changes: MutableList<DBEntry>, private val dao: TemporaryTargetDao) : DelegatedDao(changes), TemporaryTargetDao by dao {

    override suspend fun insertNewEntry(entry: TemporaryTarget): Long {
        changes.add(entry)
        return dao.insertNewEntry(entry)
    }

    override suspend fun updateExistingEntry(entry: TemporaryTarget): Long {
        changes.add(entry)
        return dao.updateExistingEntry(entry)
    }
}
