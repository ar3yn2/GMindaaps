package app.gmaaps.database.transactions

import app.gmaaps.database.entities.APSResult

/**
 * Creates or updates the ApsResult
 */
class InsertOrUpdateApsResultTransaction(
    private val apsResult: APSResult
) : Transaction<InsertOrUpdateApsResultTransaction.TransactionResult>() {

    override suspend fun run(): TransactionResult {
        val result = TransactionResult()
        val current = database.apsResultDao.findById(apsResult.id)
        if (current == null) {
            database.apsResultDao.insertNewEntry(apsResult)
            result.inserted.add(apsResult)
        } else {
            database.apsResultDao.updateExistingEntry(apsResult)
            result.updated.add(apsResult)
        }
        return result
    }

    class TransactionResult {

        val inserted = mutableListOf<APSResult>()
        val updated = mutableListOf<APSResult>()
    }
}
