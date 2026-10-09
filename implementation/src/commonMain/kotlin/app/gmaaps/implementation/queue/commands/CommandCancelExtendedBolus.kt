package app.gmaaps.implementation.queue.commands

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.queue.Command
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.ui.CoreUiStrings

class CommandCancelExtendedBolus(
    private val aapsLogger: AAPSLogger,
    private val rh: TextResolver,
    private val activePlugin: ActivePlugin,
    override val pumpEnactResultProvider: () -> PumpEnactResult,
) : Command() {

    override val commandType: Command.CommandType = Command.CommandType.EXTENDEDBOLUS

    override suspend fun execute(): PumpEnactResult =
        activePlugin.activePump.cancelExtendedBolus().also {
            aapsLogger.debug(LTag.PUMPQUEUE, "Result success: ${it.success} enacted: ${it.enacted}")
        }

    override fun status(): String = rh.gs(CoreUiStrings.uel_cancel_extended_bolus)

    override fun log(): String = "CANCEL EXTENDEDBOLUS"
}

