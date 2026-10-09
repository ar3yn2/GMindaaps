package app.gmaaps.database.daos.delegated

import app.gmaaps.database.daos.EffectiveProfileSwitchDao
import app.gmaaps.database.entities.EffectiveProfileSwitch
import app.gmaaps.database.entities.interfaces.DBEntry

internal class DelegatedEffectiveProfileSwitchDao(changes: MutableList<DBEntry>, private val dao: EffectiveProfileSwitchDao) : DelegatedDao(changes), EffectiveProfileSwitchDao by dao {

    override suspend fun insertNewEntry(entry: EffectiveProfileSwitch): Long {
        changes.add(entry)
        return super.insertNewEntry(entry)
    }

    override suspend fun updateExistingEntry(entry: EffectiveProfileSwitch): Long {
        changes.add(entry)
        return super.updateExistingEntry(entry)
    }
}
