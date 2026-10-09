package app.gmaaps.implementation.queue.commands

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.pump.PumpSync
import app.gmaaps.core.interfaces.queue.Command
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.ui.CoreUiStrings

class CommandTempBasalAbsolute(
    private val aapsLogger: AAPSLogger,
    private val rh: TextResolver,
    private val activePlugin: ActivePlugin,
    override val pumpEnactResultProvider: () -> PumpEnactResult,
    private val absoluteRate: Double,
    private val durationInMinutes: Int,
    private val enforceNew: Boolean,
    private val tbrType: PumpSync.TemporaryBasalType,
) : Command() {

    override val commandType: Command.CommandType = Command.CommandType.TEMPBASAL

    override suspend fun execute(): PumpEnactResult =
        activePlugin.activePump.setTempBasalAbsolute(absoluteRate, durationInMinutes, enforceNew, tbrType).also {
            aapsLogger.debug(LTag.PUMPQUEUE, "Result rate: $absoluteRate durationInMinutes: $durationInMinutes success: ${it.success} enacted: ${it.enacted}")
        }

    override fun status(): String = rh.gs(CoreUiStrings.temp_basal_absolute, absoluteRate, durationInMinutes)

    override fun log(): String = "TEMP BASAL $absoluteRate U/h $durationInMinutes min"
}

