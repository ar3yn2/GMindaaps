package app.gmaaps.implementation.queue.commands

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.pump.Dana
import app.gmaaps.core.interfaces.pump.Diaconn
import app.gmaaps.core.interfaces.pump.Medtrum
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.queue.Command
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.ui.CoreUiStrings

class CommandSetUserSettings(
    private val aapsLogger: AAPSLogger,
    private val rh: TextResolver,
    private val activePlugin: ActivePlugin,
    override val pumpEnactResultProvider: () -> PumpEnactResult,
) : Command() {

    override val commandType: Command.CommandType = Command.CommandType.SET_USER_SETTINGS

    override suspend fun execute(): PumpEnactResult {
        val pump = activePlugin.activePumpInternal
        val result = when (pump) {
            is Dana    -> pump.setUserOptions()
            is Diaconn -> pump.setUserOptions()
            is Medtrum -> pump.setUserOptions()
            else       -> pumpEnactResultProvider().success(true).enacted(false)
        }
        aapsLogger.debug(LTag.PUMPQUEUE, "Result success: ${result.success} enacted: ${result.enacted}")
        return result
    }

    override fun status(): String = rh.gs(CoreUiStrings.set_user_settings)

    override fun log(): String = "SET USER SETTINGS"
}

