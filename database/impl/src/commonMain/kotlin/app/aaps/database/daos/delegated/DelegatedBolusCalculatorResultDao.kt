package app.gmaaps.database.daos.delegated

import app.gmaaps.database.daos.BolusCalculatorResultDao
import app.gmaaps.database.entities.BolusCalculatorResult
import app.gmaaps.database.entities.interfaces.DBEntry

internal class DelegatedBolusCalculatorResultDao(changes: MutableList<DBEntry>, private val dao: BolusCalculatorResultDao) : DelegatedDao(changes), BolusCalculatorResultDao by dao {

    override suspend fun insertNewEntry(entry: BolusCalculatorResult): Long {
        changes.add(entry)
        return dao.insertNewEntry(entry)
    }

    override suspend fun updateExistingEntry(entry: BolusCalculatorResult): Long {
        changes.add(entry)
        return dao.updateExistingEntry(entry)
    }
}
