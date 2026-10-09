package app.gmaaps.database.daos.delegated

import app.gmaaps.database.daos.ProfileSwitchDao
import app.gmaaps.database.entities.ProfileSwitch
import app.gmaaps.database.entities.interfaces.DBEntry

internal class DelegatedProfileSwitchDao(changes: MutableList<DBEntry>, private val dao: ProfileSwitchDao) : DelegatedDao(changes), ProfileSwitchDao by dao {

    override suspend fun insertNewEntry(entry: ProfileSwitch): Long {
        changes.add(entry)
        return dao.insertNewEntry(entry)
    }

    override suspend fun updateExistingEntry(entry: ProfileSwitch): Long {
        changes.add(entry)
        return dao.updateExistingEntry(entry)
    }
}
