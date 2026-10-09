package app.gmaaps.plugins.automation.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.navigation.ElementType
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.core.ui.CoreUiStrings
import app.gmaaps.plugins.automation.AutomationStrings
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class ActionStopProcessing(
    aapsLogger: AAPSLogger,
    rh: TextResolver,
    pumpEnactResultProvider: () -> PumpEnactResult
) : Action(aapsLogger, rh, pumpEnactResultProvider) {

    override fun friendlyName(): TextRef = AutomationStrings.stop_processing
    override fun shortDescription(): String = rh.gs(AutomationStrings.stop_processing)
    override fun composeIcon() = Icons.Filled.Stop
    override fun elementType() = ElementType.BG_CHECK

    override fun isValid(): Boolean = true

    override suspend fun doAction(): PumpEnactResult {
        return pumpEnactResultProvider().success(true).comment(CoreUiStrings.ok)
    }

    override fun toJSON(): String {
        return buildJsonObject {
            put("type", this@ActionStopProcessing::class.simpleName)
            put("data", buildJsonObject { })
        }.toString()
    }

    override fun fromJSON(data: String): Action = this

    override fun hasDialog(): Boolean = false
}
