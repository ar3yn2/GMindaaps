package app.gmaaps.database.transactions

import app.gmaaps.database.entities.TherapyEvent

class InvalidateTherapyEventTransaction(val id: Long) : Transaction<InvalidateTherapyEventTransaction.TransactionResult>() {

    override suspend fun run(): TransactionResult {
        val result = TransactionResult()
        val therapyEvent = database.therapyEventDao.findById(id)
            ?: throw IllegalArgumentException("There is no such TherapyEvent with the specified ID.")
        if (therapyEvent.isValid) {
            therapyEvent.isValid = false
            database.therapyEventDao.updateExistingEntry(therapyEvent)
            result.invalidated.add(therapyEvent)
        }
        return result
    }

    class TransactionResult {

        val invalidated = mutableListOf<TherapyEvent>()
    }
}
