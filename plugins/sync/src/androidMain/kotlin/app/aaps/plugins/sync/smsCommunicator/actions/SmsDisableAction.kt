package app.gmaaps.plugins.sync.smsCommunicator.actions

import app.gmaaps.core.data.ue.Action
import app.gmaaps.core.data.ue.Sources
import app.gmaaps.core.data.ue.ValueWithUnit
import app.gmaaps.core.interfaces.logging.UserEntryLogger
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.smsCommunicator.Sms
import app.gmaaps.core.keys.BooleanKey
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.plugins.sync.SyncStrings
import app.gmaaps.plugins.sync.smsCommunicator.SmsAction

/** Disables remote SMS commands: SMS STOP/DISABLE. */
class SmsDisableAction(
    private val receivedSms: Sms,
    private val preferences: Preferences,
    private val rh: TextResolver,
    private val uel: UserEntryLogger,
    private val sendSMSToAllNumbers: (Sms) -> Unit
) : SmsAction(pumpCommand = false) {

    override suspend fun run() {
        preferences.put(BooleanKey.SmsAllowRemoteCommands, false)
        val replyText = rh.gs(SyncStrings.smscommunicator_stopped_sms)
        sendSMSToAllNumbers(Sms(receivedSms.phoneNumber, replyText))
        uel.log(
            Action.STOP_SMS, Sources.SMS, rh.gs(SyncStrings.smscommunicator_stopped_sms),
            ValueWithUnit.SimpleString(rh.gsNotLocalised(SyncStrings.smscommunicator_stopped_sms))
        )
    }
}

