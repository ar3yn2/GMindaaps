package app.gmaaps.plugins.sync.smsCommunicator.actions

import app.gmaaps.core.data.ue.Action
import app.gmaaps.core.data.ue.Sources
import app.gmaaps.core.data.ue.ValueWithUnit
import app.gmaaps.core.interfaces.logging.UserEntryLogger
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.smsCommunicator.Sms
import app.gmaaps.core.interfaces.sync.XDripBroadcast
import app.gmaaps.plugins.sync.SyncStrings
import app.gmaaps.plugins.sync.smsCommunicator.SmsAction

/** Sends a calibration to xDrip: CAL <value>. */
class CalibrationAction(
    val value: Double,
    private val receivedSms: Sms,
    private val xDripBroadcast: XDripBroadcast,
    private val rh: TextResolver,
    private val uel: UserEntryLogger,
    private val sendSMSToAllNumbers: (Sms) -> Unit
) : SmsAction(pumpCommand = false) {

    override suspend fun run() {
        val result = xDripBroadcast.sendCalibration(value)
        val replyText =
            if (result) rh.gs(SyncStrings.smscommunicator_calibration_sent) else rh.gs(SyncStrings.smscommunicator_calibration_failed)
        sendSMSToAllNumbers(Sms(receivedSms.phoneNumber, replyText))
        if (result)
            uel.log(
                Action.CALIBRATION, Sources.SMS, rh.gs(SyncStrings.smscommunicator_calibration_sent),
                ValueWithUnit.SimpleString(rh.gsNotLocalised(SyncStrings.smscommunicator_calibration_sent))
            )
        else
            uel.log(
                Action.CALIBRATION, Sources.SMS, rh.gs(SyncStrings.smscommunicator_calibration_failed),
                ValueWithUnit.SimpleString(rh.gsNotLocalised(SyncStrings.smscommunicator_calibration_failed))
            )
    }
}

