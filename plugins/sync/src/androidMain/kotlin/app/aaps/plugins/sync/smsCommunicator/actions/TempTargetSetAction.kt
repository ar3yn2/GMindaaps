package app.gmaaps.plugins.sync.smsCommunicator.actions

import app.gmaaps.core.data.model.GlucoseUnit
import app.gmaaps.core.data.model.TT
import app.gmaaps.core.interfaces.bolus.BatchAction
import app.gmaaps.core.interfaces.bolus.WizardBolusExecutor
import app.gmaaps.core.interfaces.profile.ProfileUtil
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.smsCommunicator.Sms
import app.gmaaps.core.interfaces.smsCommunicator.SmsCommunicator
import app.gmaaps.core.interfaces.tempTargets.ttDurationMinutes
import app.gmaaps.core.interfaces.tempTargets.ttTargetMgdl
import app.gmaaps.core.interfaces.utils.DecimalFormatter
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.plugins.sync.SyncStrings
import app.gmaaps.plugins.sync.smsCommunicator.SmsAction
import app.gmaaps.plugins.sync.smsCommunicator.SmsBatchResult
import app.gmaaps.plugins.sync.smsCommunicator.runSmsBatch

/** Activates a preset temp target through [WizardBolusExecutor]: TARGET MEAL/ACTIVITY/HYPO. */
class TempTargetSetAction(
    private val reason: TT.Reason,
    private val receivedSms: Sms,
    private val wizardBolusExecutor: WizardBolusExecutor,
    private val preferences: Preferences,
    private val profileUtil: ProfileUtil,
    private val decimalFormatter: DecimalFormatter,
    private val rh: TextResolver,
    private val smsCommunicator: SmsCommunicator,
    private val sendSMSToAllNumbers: (Sms) -> Unit
) : SmsAction(pumpCommand = false) {

    override suspend fun run() {
        val ttDuration = preferences.ttDurationMinutes(reason)
        val ttMgdl = preferences.ttTargetMgdl(reason)
        val target = BatchAction.TempTarget(reason = reason.text, lowMgdl = ttMgdl, highMgdl = ttMgdl, durationMinutes = ttDuration, startOffsetMinutes = 0)
        when (val result = wizardBolusExecutor.runSmsBatch(listOf(target))) {
            is SmsBatchResult.Done    -> {
                val units = profileUtil.units
                val ttDisplay = profileUtil.fromMgdlToUnits(ttMgdl, units)
                val ttString = if (units == GlucoseUnit.MMOL) decimalFormatter.to1Decimal(ttDisplay) else decimalFormatter.to0Decimal(ttDisplay)
                sendSMSToAllNumbers(Sms(receivedSms.phoneNumber, rh.gs(SyncStrings.smscommunicator_tt_set, ttString, ttDuration)))
            }

            is SmsBatchResult.NotDone ->
                smsCommunicator.sendSMS(Sms(receivedSms.phoneNumber, result.reply(rh.gs(SyncStrings.smscommunicator_remote_command_not_possible))))
        }
    }
}

