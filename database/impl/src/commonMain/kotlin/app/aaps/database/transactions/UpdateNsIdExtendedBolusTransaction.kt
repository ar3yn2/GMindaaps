package app.gmaaps.database.transactions

import app.gmaaps.database.entities.ExtendedBolus

class UpdateNsIdExtendedBolusTransaction(val boluses: List<ExtendedBolus>) : Transaction<UpdateNsIdExtendedBolusTransaction.TransactionResult>() {

    val result = TransactionResult()
    override suspend fun run(): TransactionResult {
        for (bolus in boluses) {
            val current = database.extendedBolusDao.findById(bolus.id)
            if (current != null && current.interfaceIDs.nightscoutId != bolus.interfaceIDs.nightscoutId) {
                current.interfaceIDs.nightscoutId = bolus.interfaceIDs.nightscoutId
                database.extendedBolusDao.updateExistingEntry(current)
                result.updatedNsId.add(current)
            }
        }
        return result
    }

    class TransactionResult {

        val updatedNsId = mutableListOf<ExtendedBolus>()
    }
}
