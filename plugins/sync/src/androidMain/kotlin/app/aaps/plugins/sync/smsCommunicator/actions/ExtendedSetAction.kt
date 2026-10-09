package app.gmaaps.plugins.sync.smsCommunicator.actions

import app.gmaaps.core.interfaces.InterfacesStrings
import app.gmaaps.core.interfaces.bolus.BatchAction
import app.gmaaps.core.interfaces.bolus.WizardBolusExecutor
import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.smsCommunicator.Sms
import app.gmaaps.core.interfaces.smsCommunicator.SmsCommunicator
import app.gmaaps.plugins.sync.SyncStrings
import app.gmaaps.plugins.sync.smsCommunicator.SmsAction
import app.gmaaps.plugins.sync.smsCommunicator.SmsBatchResult
import app.gmaaps.plugins.sync.smsCommunicator.runSmsBatch

/** Delivers an extended bolus through [WizardBolusExecutor], which caps and gates it: EXTENDED <U> <minutes>. */
class ExtendedSetAction(
    private val insulin: Double,
    private val durationMinutes: Int,
    private val receivedSms: Sms,
    private val wizardBolusExecutor: WizardBolusExecutor,
    private val config: Config,
    private val rh: TextResolver,
    private val smsCommunicator: SmsCommunicator,
    private val sendSMSToAllNumbers: (Sms) -> Unit,
    private val shortStatusBlocking: () -> String
) : SmsAction(pumpCommand = true) {

    override suspend fun run() {
        val extended = BatchAction.ExtendedBolus(insulin = insulin, durationMinutes = durationMinutes)
        when (val result = wizardBolusExecutor.runSmsBatch(listOf(extended))) {
            is SmsBatchResult.Done    -> {
                var replyText = rh.gs(SyncStrings.smscommunicator_extended_set, insulin, durationMinutes)
                if (config.APS) replyText += "\n" + rh.gs(InterfacesStrings.loopsuspended)
                sendSMSToAllNumbers(Sms(receivedSms.phoneNumber, replyText + "\n" + shortStatusBlocking()))
            }

            is SmsBatchResult.NotDone ->
                smsCommunicator.sendSMS(Sms(receivedSms.phoneNumber, result.reply(rh.gs(SyncStrings.smscommunicator_extended_failed), shortStatusBlocking)))
        }
    }
}

