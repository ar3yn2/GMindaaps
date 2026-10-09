package app.gmaaps.database.daos.delegated

import app.gmaaps.database.daos.ExtendedBolusDao
import app.gmaaps.database.entities.ExtendedBolus
import app.gmaaps.database.entities.interfaces.DBEntry

internal class DelegatedExtendedBolusDao(changes: MutableList<DBEntry>, private val dao: ExtendedBolusDao) : DelegatedDao(changes), ExtendedBolusDao by dao {

    override suspend fun insertNewEntry(entry: ExtendedBolus): Long {
        changes.add(entry)
        return dao.insertNewEntry(entry)
    }

    override suspend fun updateExistingEntry(entry: ExtendedBolus): Long {
        changes.add(entry)
        return dao.updateExistingEntry(entry)
    }
}
