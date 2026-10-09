package app.gmaaps.implementation.queue.commands

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.pump.PumpSync
import app.gmaaps.core.interfaces.queue.Command
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.ui.CoreUiStrings

class CommandCancelTempBasal(
    private val aapsLogger: AAPSLogger,
    private val rh: TextResolver,
    private val activePlugin: ActivePlugin,
    private val pumpSync: PumpSync,
    private val dateUtil: DateUtil,
    override val pumpEnactResultProvider: () -> PumpEnactResult,
    private val enforceNew: Boolean,
    /** true if called by detection of pump in suspend mode */
    private val autoForced: Boolean,
) : Command() {

    override val commandType: Command.CommandType = Command.CommandType.TEMPBASAL

    override suspend fun execute(): PumpEnactResult {
        val r = activePlugin.activePump.cancelTempBasal(enforceNew)
        /*
            If this command is auto-forced, it means pump is in suspended mode
            and may not be able to accept temp basal.
            In this case ignore error reported by pump and
            cancel tbr anyway
         */
        if (autoForced && !r.success) {
            if (pumpSync.expectedPumpState().temporaryBasal != null) {
                pumpSync.syncStopTemporaryBasalWithPumpId(
                    dateUtil.now(),
                    dateUtil.now(),
                    activePlugin.activePump.pumpDescription.pumpType,
                    activePlugin.activePump.serialNumber(),
                    ignorePumpIds = true
                )
                aapsLogger.debug(LTag.PUMPQUEUE, "Stopping TBR from suspended pump (auto-forced)")
            }
            r.success(true).enacted(false)
        }
        aapsLogger.debug(LTag.PUMPQUEUE, "Result success: ${r.success} enacted: ${r.enacted}")
        return r
    }

    override fun status(): String = rh.gs(CoreUiStrings.uel_accepts_temp_basal)

    override fun log(): String = "CANCEL TEMPBASAL"
}

