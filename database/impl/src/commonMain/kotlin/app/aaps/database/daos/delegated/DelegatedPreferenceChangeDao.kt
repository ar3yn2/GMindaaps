package app.gmaaps.database.daos.delegated

import app.gmaaps.database.daos.PreferenceChangeDao
import app.gmaaps.database.entities.PreferenceChange
import app.gmaaps.database.entities.interfaces.DBEntry

internal class DelegatedPreferenceChangeDao(changes: MutableList<DBEntry>, private val dao: PreferenceChangeDao) : DelegatedDao(changes), PreferenceChangeDao by dao {

    override suspend fun insert(preferenceChange: PreferenceChange) {
        changes.add(preferenceChange)
        return dao.insert(preferenceChange)
    }

}
