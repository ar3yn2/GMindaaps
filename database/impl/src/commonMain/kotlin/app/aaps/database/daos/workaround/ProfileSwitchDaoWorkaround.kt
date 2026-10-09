package app.gmaaps.database.daos.workaround

import androidx.room.Transaction
import app.gmaaps.database.daos.ProfileSwitchDao
import app.gmaaps.database.daos.TraceableDao
import app.gmaaps.database.daos.insertNewEntryImpl
import app.gmaaps.database.daos.updateExistingEntryImpl
import app.gmaaps.database.entities.ProfileSwitch

internal interface ProfileSwitchDaoWorkaround : TraceableDao<ProfileSwitch> {

    @Transaction
    override suspend fun insertNewEntry(entry: ProfileSwitch): Long =
        (this as ProfileSwitchDao).insertNewEntryImpl(entry)

    @Transaction
    override suspend fun updateExistingEntry(entry: ProfileSwitch): Long =
        (this as ProfileSwitchDao).updateExistingEntryImpl(entry)
}

