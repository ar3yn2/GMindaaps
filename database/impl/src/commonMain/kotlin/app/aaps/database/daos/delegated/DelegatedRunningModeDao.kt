package app.gmaaps.database.daos.delegated

import app.gmaaps.database.daos.RunningModeDao
import app.gmaaps.database.entities.RunningMode
import app.gmaaps.database.entities.interfaces.DBEntry

internal class DelegatedRunningModeDao(changes: MutableList<DBEntry>, private val dao: RunningModeDao) : DelegatedDao(changes), RunningModeDao by dao {

    override suspend fun insertNewEntry(entry: RunningMode): Long {
        changes.add(entry)
        return super.insertNewEntry(entry)
    }

    override suspend fun updateExistingEntry(entry: RunningMode): Long {
        changes.add(entry)
        return super.updateExistingEntry(entry)
    }
}
