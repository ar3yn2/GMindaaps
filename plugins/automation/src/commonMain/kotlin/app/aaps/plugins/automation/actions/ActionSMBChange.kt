package app.gmaaps.plugins.automation.actions

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.navigation.ElementType
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.keys.BooleanKey
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.core.ui.CoreUiStrings
import app.gmaaps.core.ui.compose.icons.IcSmb
import app.gmaaps.core.utils.lenientBoolean
import app.gmaaps.plugins.automation.AutomationStrings
import app.gmaaps.plugins.automation.elements.InputDropdownOnOffMenu
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class ActionSMBChange(
    aapsLogger: AAPSLogger,
    rh: TextResolver,
    pumpEnactResultProvider: () -> PumpEnactResult,
    private val dateUtil: DateUtil,
    private val preferences: Preferences
) : Action(aapsLogger, rh, pumpEnactResultProvider) {


    var smbState: InputDropdownOnOffMenu = InputDropdownOnOffMenu(rh, true)

    override fun friendlyName(): TextRef = AutomationStrings.changeSmbState
    override fun shortDescription(): String = rh.gs(AutomationStrings.changeSmbTo, smbState.toTextValue())
    override fun composeIcon() = IcSmb
    override fun elementType() = ElementType.INSULIN

    override suspend fun doAction(): PumpEnactResult {
        preferences.put(BooleanKey.ApsUseSmb, smbState.value)
        return pumpEnactResultProvider().success(true).comment(CoreUiStrings.ok)
    }

    override fun hasDialog(): Boolean = true

    override fun toJSON(): String {
        val data = buildJsonObject { put("smbState", smbState.value) }
        return buildJsonObject {
            put("type", this@ActionSMBChange::class.simpleName)
            put("data", data)
        }.toString()
    }

    override fun fromJSON(data: String): Action {
        val o = jsonOf(data)
        smbState.value = o.lenientBoolean("smbState", true)
        return this
    }

    override fun isValid(): Boolean = true
}
