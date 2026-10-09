package app.gmaaps.plugins.sync.smsCommunicator.actions

import app.gmaaps.core.data.ue.Action
import app.gmaaps.core.data.ue.Sources
import app.gmaaps.core.data.ue.ValueWithUnit
import app.gmaaps.core.interfaces.configuration.ConfigBuilder
import app.gmaaps.core.interfaces.logging.UserEntryLogger
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.smsCommunicator.Sms
import app.gmaaps.core.interfaces.smsCommunicator.SmsCommunicator
import app.gmaaps.plugins.sync.SyncStrings
import app.gmaaps.plugins.sync.smsCommunicator.SmsAction

/** Restarts AAPS: RESTART. */
class RestartAction(
    private val receivedSms: Sms,
    private val rh: TextResolver,
    private val uel: UserEntryLogger,
    private val configBuilder: ConfigBuilder,
    private val smsCommunicator: SmsCommunicator
) : SmsAction(pumpCommand = false) {

    override suspend fun run() {
        uel.log(
            Action.EXIT_AAPS, Sources.SMS,
            rh.gs(SyncStrings.smscommunicator_restarting),
            ValueWithUnit.SimpleString(rh.gsNotLocalised(SyncStrings.smscommunicator_restarting))
        )
        smsCommunicator.sendSMS(Sms(receivedSms.phoneNumber, rh.gs(SyncStrings.smscommunicator_restarting)))
        configBuilder.exitApp("SMS", Sources.SMS, true)
    }
}

