package app.gmaaps.implementation.queue.commands

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.pump.Insight
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.queue.Command
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.ui.CoreUiStrings

class CommandStartPump(
    private val aapsLogger: AAPSLogger,
    private val rh: TextResolver,
    private val activePlugin: ActivePlugin,
    override val pumpEnactResultProvider: () -> PumpEnactResult,
) : Command() {

    override val commandType: Command.CommandType = Command.CommandType.START_PUMP

    override suspend fun execute(): PumpEnactResult {
        val pump = activePlugin.activePumpInternal
        return if (pump is Insight) {
            pump.startPump().also {
                aapsLogger.debug(LTag.PUMPQUEUE, "Result success: ${it.success} enacted: ${it.enacted}")
            }
        } else {
            pumpEnactResultProvider().success(true).enacted(false)
        }
    }

    override fun status(): String = rh.gs(CoreUiStrings.start_pump)

    override fun log(): String = "START PUMP"
}

