package app.gmaaps.plugins.sync.smsCommunicator

abstract class SmsAction(val pumpCommand: Boolean) {

    abstract suspend fun run()
}

