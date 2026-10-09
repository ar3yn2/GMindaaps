package app.gmaaps.plugins.sync.smsCommunicator.actions

import app.gmaaps.core.interfaces.bolus.BatchAction
import app.gmaaps.core.interfaces.bolus.WizardBolusExecutor
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.smsCommunicator.Sms
import app.gmaaps.core.interfaces.smsCommunicator.SmsCommunicator
import app.gmaaps.plugins.sync.SyncStrings
import app.gmaaps.plugins.sync.smsCommunicator.SmsAction
import app.gmaaps.plugins.sync.smsCommunicator.SmsBatchResult
import app.gmaaps.plugins.sync.smsCommunicator.runSmsBatch

/** Cancels an extended bolus through [WizardBolusExecutor]: EXTENDED CANCEL/STOP. */
class ExtendedCancelAction(
    private val receivedSms: Sms,
    private val wizardBolusExecutor: WizardBolusExecutor,
    private val rh: TextResolver,
    private val smsCommunicator: SmsCommunicator,
    private val sendSMSToAllNumbers: (Sms) -> Unit,
    private val shortStatusBlocking: () -> String
) : SmsAction(pumpCommand = true) {

    override suspend fun run() {
        when (val result = wizardBolusExecutor.runSmsBatch(listOf(BatchAction.CancelExtendedBolus))) {
            is SmsBatchResult.Done    ->
                sendSMSToAllNumbers(Sms(receivedSms.phoneNumber, rh.gs(SyncStrings.smscommunicator_extended_canceled) + "\n" + shortStatusBlocking()))

            is SmsBatchResult.NotDone ->
                smsCommunicator.sendSMS(Sms(receivedSms.phoneNumber, result.reply(rh.gs(SyncStrings.smscommunicator_extended_cancel_failed), shortStatusBlocking)))
        }
    }
}

