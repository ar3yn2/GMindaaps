package app.gmaaps.database.daos.delegated

import app.gmaaps.database.daos.StepsCountDao
import app.gmaaps.database.entities.StepsCount
import app.gmaaps.database.entities.interfaces.DBEntry

internal class DelegatedStepsCountDao(
    changes: MutableList<DBEntry>,
    private val dao: StepsCountDao
) : DelegatedDao(changes), StepsCountDao by dao {

    override suspend fun insertNewEntry(entry: StepsCount): Long {
        changes.add(entry)
        return dao.insertNewEntry(entry)
    }

    override suspend fun updateExistingEntry(entry: StepsCount): Long {
        changes.add(entry)
        return dao.updateExistingEntry(entry)
    }
}

