package app.gmaaps.database.transactions

import app.gmaaps.database.entities.BolusCalculatorResult

class InvalidateBolusCalculatorResultTransaction(val id: Long) : Transaction<InvalidateBolusCalculatorResultTransaction.TransactionResult>() {

    override suspend fun run(): TransactionResult {
        val result = TransactionResult()
        val bolusCalculatorResult = database.bolusCalculatorResultDao.findById(id)
            ?: throw IllegalArgumentException("There is no such BolusCalculatorResult with the specified ID.")
        if (bolusCalculatorResult.isValid) {
            bolusCalculatorResult.isValid = false
            database.bolusCalculatorResultDao.updateExistingEntry(bolusCalculatorResult)
            result.invalidated.add(bolusCalculatorResult)
        }
        return result
    }

    class TransactionResult {

        val invalidated = mutableListOf<BolusCalculatorResult>()
    }
}
