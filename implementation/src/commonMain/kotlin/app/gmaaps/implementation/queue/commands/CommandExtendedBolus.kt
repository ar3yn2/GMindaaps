package app.gmaaps.implementation.queue.commands

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.queue.Command
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.ui.CoreUiStrings

class CommandExtendedBolus(
    private val aapsLogger: AAPSLogger,
    private val rh: TextResolver,
    private val activePlugin: ActivePlugin,
    override val pumpEnactResultProvider: () -> PumpEnactResult,
    private val insulin: Double,
    private val durationInMinutes: Int,
) : Command() {

    override val commandType: Command.CommandType = Command.CommandType.EXTENDEDBOLUS

    override suspend fun execute(): PumpEnactResult =
        activePlugin.activePump.setExtendedBolus(insulin, durationInMinutes).also {
            aapsLogger.debug(LTag.PUMPQUEUE, "Result rate: $insulin durationInMinutes: $durationInMinutes success: ${it.success} enacted: ${it.enacted}")
        }

    override fun status(): String = rh.gs(CoreUiStrings.extended_bolus_u_min, insulin, durationInMinutes)

    override fun log(): String = "EXTENDEDBOLUS $insulin U $durationInMinutes min"
}

