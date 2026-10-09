package app.gmaaps.database.daos.delegated

import app.gmaaps.database.daos.TotalDailyDoseDao
import app.gmaaps.database.entities.TotalDailyDose
import app.gmaaps.database.entities.interfaces.DBEntry

internal class DelegatedTotalDailyDoseDao(changes: MutableList<DBEntry>, private val dao: TotalDailyDoseDao) : DelegatedDao(changes), TotalDailyDoseDao by dao {

    override suspend fun insertNewEntry(entry: TotalDailyDose): Long {
        changes.add(entry)
        return dao.insertNewEntry(entry)
    }

    override suspend fun updateExistingEntry(entry: TotalDailyDose): Long {
        changes.add(entry)
        return dao.updateExistingEntry(entry)
    }
}
