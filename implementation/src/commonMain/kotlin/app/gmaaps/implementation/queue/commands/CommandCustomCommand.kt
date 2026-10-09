package app.gmaaps.implementation.queue.commands

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.queue.Command
import app.gmaaps.core.interfaces.queue.CustomCommand

class CommandCustomCommand(
    private val aapsLogger: AAPSLogger,
    private val activePlugin: ActivePlugin,
    override val pumpEnactResultProvider: () -> PumpEnactResult,
    val customCommand: CustomCommand,
) : Command() {

    override val commandType: Command.CommandType = Command.CommandType.CUSTOM_COMMAND

    override suspend fun execute(): PumpEnactResult {
        val r = activePlugin.activePump.executeCustomCommand(customCommand)
            ?: pumpEnactResultProvider().success(true).enacted(false)
        aapsLogger.debug(LTag.PUMPQUEUE, "Result success: ${r.success} enacted: ${r.enacted}")
        return r
    }

    override fun status(): String = customCommand.statusDescription

    override fun log(): String = customCommand.statusDescription
}

