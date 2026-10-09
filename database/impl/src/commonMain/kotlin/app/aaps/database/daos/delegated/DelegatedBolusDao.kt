package app.gmaaps.database.daos.delegated

import app.gmaaps.database.daos.BolusDao
import app.gmaaps.database.entities.Bolus
import app.gmaaps.database.entities.interfaces.DBEntry

internal class DelegatedBolusDao(changes: MutableList<DBEntry>, private val dao: BolusDao) : DelegatedDao(changes), BolusDao by dao {

    override suspend fun insertNewEntry(entry: Bolus): Long {
        changes.add(entry)
        return dao.insertNewEntry(entry)
    }

    override suspend fun updateExistingEntry(entry: Bolus): Long {
        changes.add(entry)
        return dao.updateExistingEntry(entry)
    }
}
