package app.gmaaps.implementation.queue.commands

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.pump.Dana
import app.gmaaps.core.interfaces.pump.Diaconn
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.queue.Command
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.ui.CoreUiStrings

class CommandLoadHistory(
    private val aapsLogger: AAPSLogger,
    private val rh: TextResolver,
    private val activePlugin: ActivePlugin,
    override val pumpEnactResultProvider: () -> PumpEnactResult,
    private val type: Byte,
) : Command() {

    override val commandType: Command.CommandType = Command.CommandType.LOAD_HISTORY

    override suspend fun execute(): PumpEnactResult {
        val pump = activePlugin.activePumpInternal
        val result = when (pump) {
            is Dana    -> pump.loadHistory(type)
            is Diaconn -> pump.loadHistory()
            else       -> pumpEnactResultProvider().success(true).enacted(false)
        }
        aapsLogger.debug(LTag.PUMPQUEUE, "Result success: ${result.success} enacted: ${result.enacted}")
        return result
    }

    override fun status(): String = rh.gs(CoreUiStrings.load_history, type.toInt())

    override fun log(): String = "LOAD HISTORY $type"
}

