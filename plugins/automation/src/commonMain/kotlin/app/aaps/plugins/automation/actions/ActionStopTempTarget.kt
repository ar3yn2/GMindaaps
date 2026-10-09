package app.gmaaps.plugins.automation.actions

import app.gmaaps.core.data.ue.Sources
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.navigation.ElementType
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.core.ui.CoreUiStrings
import app.gmaaps.core.ui.compose.icons.IcTtCancel

class ActionStopTempTarget(
    aapsLogger: AAPSLogger,
    rh: TextResolver,
    pumpEnactResultProvider: () -> PumpEnactResult,
    private val persistenceLayer: PersistenceLayer,
    private val dateUtil: DateUtil
) : Action(aapsLogger, rh, pumpEnactResultProvider) {


    override fun friendlyName(): TextRef = CoreUiStrings.stoptemptarget
    override fun shortDescription(): String = rh.gs(CoreUiStrings.stoptemptarget)
    override fun composeIcon() = IcTtCancel
    override fun elementType() = ElementType.TEMP_TARGET_MANAGEMENT

    override suspend fun doAction(): PumpEnactResult {
        persistenceLayer.cancelCurrentTemporaryTargetIfAny(dateUtil.now(), app.gmaaps.core.data.ue.Action.CANCEL_TT, Sources.Automation, title, listOf())
        return pumpEnactResultProvider().success(true).comment(CoreUiStrings.ok)
    }

    override fun isValid(): Boolean = true
}

