package app.gmaaps.database.daos.delegated

import app.gmaaps.database.daos.TherapyEventDao
import app.gmaaps.database.entities.TherapyEvent
import app.gmaaps.database.entities.interfaces.DBEntry

internal class DelegatedTherapyEventDao(changes: MutableList<DBEntry>, private val dao: TherapyEventDao) : DelegatedDao(changes), TherapyEventDao by dao {

    override suspend fun insertNewEntry(entry: TherapyEvent): Long {
        changes.add(entry)
        return dao.insertNewEntry(entry)
    }

    override suspend fun updateExistingEntry(entry: TherapyEvent): Long {
        changes.add(entry)
        return dao.updateExistingEntry(entry)
    }
}
