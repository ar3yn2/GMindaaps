package app.gmaaps.database.transactions

import app.gmaaps.database.entities.TherapyEvent

class InsertIfNewByTimestampTherapyEventTransaction(
    val therapyEvent: TherapyEvent
) : Transaction<InsertIfNewByTimestampTherapyEventTransaction.TransactionResult>() {

    override suspend fun run(): TransactionResult {
        val result = TransactionResult()
        val current = database.therapyEventDao.findByTimestamp(therapyEvent.type, therapyEvent.timestamp)
        if (current == null) {
            database.therapyEventDao.insertNewEntry(therapyEvent)
            result.inserted.add(therapyEvent)
        } else result.existing.add(therapyEvent)
        return result
    }

    class TransactionResult {

        val inserted = mutableListOf<TherapyEvent>()
        val existing = mutableListOf<TherapyEvent>()
    }
}
