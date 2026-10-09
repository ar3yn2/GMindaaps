package app.gmaaps.database.daos.delegated

import app.gmaaps.database.daos.VersionChangeDao
import app.gmaaps.database.entities.VersionChange
import app.gmaaps.database.entities.interfaces.DBEntry

internal class DelegatedVersionChangeDao(changes: MutableList<DBEntry>, private val dao: VersionChangeDao) : DelegatedDao(changes), VersionChangeDao by dao {

    override suspend fun insert(versionChange: VersionChange) {
        changes.add(versionChange)
        return dao.insert(versionChange)
    }

}
