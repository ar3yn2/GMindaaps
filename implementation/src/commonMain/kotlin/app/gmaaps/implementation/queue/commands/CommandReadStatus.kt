package app.gmaaps.implementation.queue.commands

import app.gmaaps.core.data.time.T
import app.gmaaps.core.interfaces.alerts.LocalAlertUtils
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.queue.Command
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.ui.CoreUiStrings
import kotlin.time.Clock

class CommandReadStatus(
    private val aapsLogger: AAPSLogger,
    private val rh: TextResolver,
    private val activePlugin: ActivePlugin,
    private val localAlertUtils: LocalAlertUtils,
    override val pumpEnactResultProvider: () -> PumpEnactResult,
    val reason: String,
) : Command() {

    override val commandType: Command.CommandType = Command.CommandType.READSTATUS

    override suspend fun execute(): PumpEnactResult {
        activePlugin.activePump.getPumpStatus(reason)
        localAlertUtils.reportPumpStatusRead()
        aapsLogger.debug(LTag.PUMPQUEUE, "CommandReadStatus executed. Reason: $reason")
        val pump = activePlugin.activePump
        val result = pumpEnactResultProvider().success(false)
        val lastConnection = pump.lastDataTime.value
        if (lastConnection > Clock.System.now().toEpochMilliseconds() - T.mins(1).msecs()) result.success(true)
        return result
    }

    override fun status(): String = rh.gs(CoreUiStrings.read_status, reason)

    override fun log(): String = "READSTATUS $reason"
}

