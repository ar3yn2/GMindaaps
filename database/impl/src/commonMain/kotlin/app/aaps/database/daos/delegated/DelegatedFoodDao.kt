package app.gmaaps.database.daos.delegated

import app.gmaaps.database.daos.FoodDao
import app.gmaaps.database.entities.Food
import app.gmaaps.database.entities.interfaces.DBEntry

internal class DelegatedFoodDao(changes: MutableList<DBEntry>, private val dao: FoodDao) : DelegatedDao(changes), FoodDao by dao {

    override suspend fun insertNewEntry(entry: Food): Long {
        changes.add(entry)
        return dao.insertNewEntry(entry)
    }

    override suspend fun updateExistingEntry(entry: Food): Long {
        changes.add(entry)
        return dao.updateExistingEntry(entry)
    }
}
