package app.gmaaps.database.transactions

import app.gmaaps.database.DelegatedAppDatabase

/**
 * Base class for database transactions
 * @param T The return type of the Transaction
 */
abstract class Transaction<T> {

    /**
     * Executes the Transaction
     */
    internal abstract suspend fun run(): T

    internal lateinit var database: DelegatedAppDatabase

}
