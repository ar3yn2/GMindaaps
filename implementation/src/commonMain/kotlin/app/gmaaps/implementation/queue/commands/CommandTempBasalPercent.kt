package app.gmaaps.implementation.queue.commands

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.pump.PumpSync
import app.gmaaps.core.interfaces.queue.Command
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.ui.CoreUiStrings

class CommandTempBasalPercent(
    private val aapsLogger: AAPSLogger,
    private val rh: TextResolver,
    private val activePlugin: ActivePlugin,
    override val pumpEnactResultProvider: () -> PumpEnactResult,
    private val percent: Int,
    private val durationInMinutes: Int,
    private val enforceNew: Boolean,
    private val tbrType: PumpSync.TemporaryBasalType,
) : Command() {

    override val commandType: Command.CommandType = Command.CommandType.TEMPBASAL

    override suspend fun execute(): PumpEnactResult {
        val r = if (percent == 100)
            activePlugin.activePump.cancelTempBasal(enforceNew)
        else
            activePlugin.activePump.setTempBasalPercent(percent, durationInMinutes, enforceNew, tbrType)
        aapsLogger.debug(LTag.PUMPQUEUE, "Result percent: $percent durationInMinutes: $durationInMinutes success: ${r.success} enacted: ${r.enacted}")
        return r
    }

    override fun status(): String = rh.gs(CoreUiStrings.temp_basal_percent, percent, durationInMinutes)

    override fun log(): String = "TEMP BASAL $percent% $durationInMinutes min"
}

