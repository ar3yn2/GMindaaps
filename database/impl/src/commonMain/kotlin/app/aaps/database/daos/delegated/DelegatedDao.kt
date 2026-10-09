package app.gmaaps.database.daos.delegated

import app.gmaaps.database.entities.interfaces.DBEntry

/**
 * A DAO that adds updated or inserted entries to a list
 */
internal abstract class DelegatedDao(protected val changes: MutableList<DBEntry>)
