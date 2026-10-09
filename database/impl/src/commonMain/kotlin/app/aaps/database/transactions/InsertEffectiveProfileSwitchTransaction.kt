package app.gmaaps.database.transactions

import app.gmaaps.database.entities.EffectiveProfileSwitch

class InsertEffectiveProfileSwitchTransaction(private val effectiveProfileSwitch: EffectiveProfileSwitch) : Transaction<InsertEffectiveProfileSwitchTransaction.TransactionResult>() {

    override suspend fun run(): TransactionResult {
        val result = TransactionResult()

        database.effectiveProfileSwitchDao.insertNewEntry(effectiveProfileSwitch)
        result.inserted.add(effectiveProfileSwitch)
        return result
    }

    class TransactionResult {

        val inserted = mutableListOf<EffectiveProfileSwitch>()
    }
}
