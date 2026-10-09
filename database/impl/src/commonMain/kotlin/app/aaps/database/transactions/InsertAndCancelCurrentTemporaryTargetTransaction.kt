package app.gmaaps.database.transactions

import app.gmaaps.database.entities.TemporaryTarget
import app.gmaaps.database.entities.interfaces.end

class InsertAndCancelCurrentTemporaryTargetTransaction(
    val temporaryTarget: TemporaryTarget
) : Transaction<InsertAndCancelCurrentTemporaryTargetTransaction.TransactionResult>() {

    override suspend fun run(): TransactionResult {
        val result = TransactionResult()
        val current = database.temporaryTargetDao.getTemporaryTargetActiveAtLegacy(temporaryTarget.timestamp)
        if (current != null) {
            current.end = temporaryTarget.timestamp
            database.temporaryTargetDao.updateExistingEntry(current)
            result.updated.add(current)
        }
        database.temporaryTargetDao.insertNewEntry(temporaryTarget)
        result.inserted.add(temporaryTarget)
        return result
    }

    class TransactionResult {

        val inserted = mutableListOf<TemporaryTarget>()
        val updated = mutableListOf<TemporaryTarget>()
    }
}
