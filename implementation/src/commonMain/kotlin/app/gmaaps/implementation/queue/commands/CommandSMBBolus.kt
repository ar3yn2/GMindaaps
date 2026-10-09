package app.gmaaps.implementation.queue.commands

import app.gmaaps.core.data.time.T
import app.gmaaps.core.interfaces.InterfacesStrings
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.pump.BolusProgressData
import app.gmaaps.core.interfaces.pump.DetailedBolusInfo
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.queue.Command
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.keys.IntKey
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.core.ui.CoreUiStrings
import kotlin.time.Clock

class CommandSMBBolus(
    private val aapsLogger: AAPSLogger,
    private val rh: TextResolver,
    private val dateUtil: DateUtil,
    private val activePlugin: ActivePlugin,
    private val persistenceLayer: PersistenceLayer,
    private val preferences: Preferences,
    private val bolusProgressData: BolusProgressData,
    override val pumpEnactResultProvider: () -> PumpEnactResult,
    private val detailedBolusInfo: DetailedBolusInfo,
    private val bolusGeneration: Long,
) : Command() {

    override val commandType: Command.CommandType = Command.CommandType.SMB_BOLUS

    override suspend fun execute(): PumpEnactResult {
        val r: PumpEnactResult
        val lastBolusTime = persistenceLayer.getNewestBolus()?.timestamp ?: 0L
        aapsLogger.debug(LTag.PUMPQUEUE, "Last bolus: $lastBolusTime ${dateUtil.dateAndTimeAndSecondsString(lastBolusTime)}")
        if (lastBolusTime != 0L && lastBolusTime + T.mins(preferences.get(IntKey.ApsMaxSmbFrequency).toLong()).msecs() > dateUtil.now()) {
            aapsLogger.debug(LTag.APS, "SMB requested but still in ${preferences.get(IntKey.ApsMaxSmbFrequency)} min interval")
            r = pumpEnactResultProvider().enacted(false).success(false).comment("SMB requested but still in ${preferences.get(IntKey.ApsMaxSmbFrequency)} min interval")
        } else if (detailedBolusInfo.deliverAtTheLatest != 0L && detailedBolusInfo.deliverAtTheLatest + T.mins(1).msecs() > Clock.System.now().toEpochMilliseconds()) {
            r = activePlugin.activePump.deliverTreatment(detailedBolusInfo)
        } else {
            r = pumpEnactResultProvider().enacted(false).success(false).comment("SMB request too old")
            aapsLogger.debug(LTag.PUMPQUEUE, "SMB bolus canceled. deliverAt: " + dateUtil.dateAndTimeString(detailedBolusInfo.deliverAtTheLatest))
        }
        aapsLogger.debug(LTag.PUMPQUEUE, "Result success: ${r.success} enacted: ${r.enacted}")
        // Generation-scoped: never wipe a newer bolus that was enqueued behind this SMB (see BolusProgressData.clear).
        bolusProgressData.clear(bolusGeneration)
        return r
    }

    override fun status(): String = rh.gs(CoreUiStrings.smb_bolus_u, detailedBolusInfo.insulin)

    override fun log(): String = "SMB BOLUS ${rh.gs(InterfacesStrings.format_insulin_units, detailedBolusInfo.insulin)}"

    override fun cancel(comment: TextRef, success: Boolean, cancelled: Boolean) {
        super.cancel(comment, success, cancelled)
        bolusProgressData.clear(bolusGeneration)
    }
}

