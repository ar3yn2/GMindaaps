package app.gmaaps.implementation.queue.commands

import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.configuration.ExternalOptions
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.profile.EffectiveProfile
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.queue.Command
import app.gmaaps.core.interfaces.queue.CommandQueue
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.smsCommunicator.SmsCommunicator
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.ui.CoreUiStrings

class CommandSetProfile(
    private val aapsLogger: AAPSLogger,
    private val rh: TextResolver,
    private val smsCommunicator: SmsCommunicator,
    private val activePlugin: ActivePlugin,
    private val dateUtil: DateUtil,
    private val commandQueue: CommandQueue,
    private val config: Config,
    private val persistenceLayer: PersistenceLayer,
    override val pumpEnactResultProvider: () -> PumpEnactResult,
    private val profile: EffectiveProfile,
    private val hasNsId: Boolean,
) : Command() {

    override val commandType: Command.CommandType = Command.CommandType.BASAL_PROFILE

    override suspend fun execute(): PumpEnactResult {
        if (commandQueue.isThisProfileSet(profile) && persistenceLayer.getEffectiveProfileSwitchActiveAt(dateUtil.now()) != null) {
            aapsLogger.debug(LTag.PUMPQUEUE, "Correct profile already set. profile: $profile")
            return pumpEnactResultProvider().success(true).enacted(false)
        }
        val r = activePlugin.activePump.setNewBasalProfile(profile)
        aapsLogger.debug(LTag.PUMPQUEUE, "Result success: ${r.success} enacted: ${r.enacted} profile: $profile")
        // Send SMS notification if ProfileSwitch is coming from NS
        val profileSwitch = persistenceLayer.getEffectiveProfileSwitchActiveAt(dateUtil.now())
        if (profileSwitch != null && r.enacted && hasNsId && !config.AAPSCLIENT) {
            if (smsCommunicator.isEnabled() && !config.isEnabled(ExternalOptions.DO_NOT_SEND_SMS_ON_PROFILE_CHANGE))
                smsCommunicator.sendNotificationToAllNumbers(rh.gs(CoreUiStrings.profile_set_ok))
        }
        return r
    }

    override fun status(): String = rh.gs(CoreUiStrings.set_profile)

    override fun log(): String = "SET PROFILE"
}

