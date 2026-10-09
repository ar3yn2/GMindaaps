package app.gmaaps.database.transactions

import app.gmaaps.database.entities.ProfileSwitch

class InsertOrUpdateProfileSwitchTransaction(val profileSwitch: ProfileSwitch) : Transaction<InsertOrUpdateProfileSwitchTransaction.TransactionResult>() {

    override suspend fun run(): TransactionResult {
        val result = TransactionResult()

        val current = database.profileSwitchDao.findById(profileSwitch.id)
        if (current == null) {
            database.profileSwitchDao.insertNewEntry(profileSwitch)
            result.inserted.add(profileSwitch)
        } else {
            database.profileSwitchDao.updateExistingEntry(profileSwitch)
            result.updated.add(profileSwitch)
        }
        return result
    }

    class TransactionResult {

        val inserted = mutableListOf<ProfileSwitch>()
        val updated = mutableListOf<ProfileSwitch>()
    }
}
