package app.gmaaps.database.daos.delegated

import app.gmaaps.database.daos.APSResultDao
import app.gmaaps.database.entities.interfaces.DBEntry

internal class DelegatedAPSResultDao(changes: MutableList<DBEntry>, private val dao: APSResultDao) : DelegatedDao(changes), APSResultDao by dao {

    override suspend fun insertNewEntry(entry: app.gmaaps.database.entities.APSResult): Long {
        changes.add(entry)
        return dao.insertNewEntry(entry)
    }

    override suspend fun updateExistingEntry(entry: app.gmaaps.database.entities.APSResult): Long {
        changes.add(entry)
        return dao.updateExistingEntry(entry)
    }
}
