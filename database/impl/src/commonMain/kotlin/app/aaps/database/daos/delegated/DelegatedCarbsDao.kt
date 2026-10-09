package app.gmaaps.database.daos.delegated

import app.gmaaps.database.daos.CarbsDao
import app.gmaaps.database.entities.Carbs
import app.gmaaps.database.entities.interfaces.DBEntry

internal class DelegatedCarbsDao(changes: MutableList<DBEntry>, private val dao: CarbsDao) : DelegatedDao(changes), CarbsDao by dao {

    override suspend fun insertNewEntry(entry: Carbs): Long {
        changes.add(entry)
        return dao.insertNewEntry(entry)
    }

    override suspend fun updateExistingEntry(entry: Carbs): Long {
        changes.add(entry)
        return dao.updateExistingEntry(entry)
    }
}
