package app.gmaaps.database.transactions

import app.gmaaps.database.entities.RunningMode
import app.gmaaps.database.entities.interfaces.end

class CancelCurrentTemporaryRunningModeIfAnyTransaction(
    val timestamp: Long
) : Transaction<CancelCurrentTemporaryRunningModeIfAnyTransaction.TransactionResult>() {

    override suspend fun run(): TransactionResult {
        val result = TransactionResult()
        val current = database.runningModeDao.getTemporaryRunningModeActiveAt(timestamp)
        if (current != null) {
            current.end = timestamp
            database.runningModeDao.updateExistingEntry(current)
            result.updated.add(current)
        }
        return result
    }

    class TransactionResult {

        val updated = mutableListOf<RunningMode>()
    }
}

