package app.gmaaps.database.daos.workaround

import androidx.room.Transaction
import app.gmaaps.database.daos.TraceableDao
import app.gmaaps.database.daos.insertNewEntryImpl
import app.gmaaps.database.daos.updateExistingEntryImpl
import app.gmaaps.database.entities.interfaces.TraceableDBEntry

interface TraceableDaoWorkaround<T : TraceableDBEntry> {

    /**
     * Inserts a new entry
     *
     * @return The ID of the newly generated entry
     */
    @Transaction
    suspend fun insertNewEntry(entry: T): Long =
        (this as TraceableDao<T>).insertNewEntryImpl(entry)

    /**
     * Updates an existing entry
     *
     * @return The ID of the newly generated HISTORIC entry
     */
    @Transaction
    suspend fun updateExistingEntry(entry: T): Long =
        (this as TraceableDao<T>).updateExistingEntryImpl(entry)
}

