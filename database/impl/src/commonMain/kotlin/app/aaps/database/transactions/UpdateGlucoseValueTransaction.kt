package app.gmaaps.database.transactions

import app.gmaaps.database.entities.GlucoseValue

/**
 * Updates the GlucoseValue
 */
class UpdateGlucoseValueTransaction(val glucoseValue: GlucoseValue) : Transaction<Unit>() {

    override suspend fun run() {
        database.glucoseValueDao.updateExistingEntry(glucoseValue)
    }
}
