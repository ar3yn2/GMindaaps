package app.gmaaps.database.daos.delegated

import app.gmaaps.database.daos.UserEntryDao
import app.gmaaps.database.entities.UserEntry
import app.gmaaps.database.entities.interfaces.DBEntry

internal class DelegatedUserEntryDao(changes: MutableList<DBEntry>, private val dao: UserEntryDao) : DelegatedDao(changes), UserEntryDao by dao {

    override suspend fun insert(userEntry: UserEntry) {
        changes.add(userEntry)
        return dao.insert(userEntry)
    }

}
