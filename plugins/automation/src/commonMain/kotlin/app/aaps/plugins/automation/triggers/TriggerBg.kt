package app.gmaaps.plugins.automation.triggers

import app.gmaaps.core.data.model.GlucoseUnit
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.navigation.ElementType
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.core.ui.CoreUiStrings
import app.gmaaps.core.ui.compose.icons.IcBgCheck
import app.gmaaps.core.utils.lenientDouble
import app.gmaaps.core.utils.lenientString
import app.gmaaps.core.utils.lenientStringOrNull
import app.gmaaps.plugins.automation.AutomationStrings
import app.gmaaps.plugins.automation.elements.Comparator
import app.gmaaps.plugins.automation.elements.InputBg
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.math.roundToInt

class TriggerBg(deps: TriggerDeps) : Trigger(deps) {

    var bg = InputBg(profileFunction)
    var comparator = Comparator(rh)

    constructor(deps: TriggerDeps, value: Double, units: GlucoseUnit, compare: Comparator.Compare) : this(deps) {
        bg = InputBg(profileFunction, value, units)
        comparator = Comparator(rh, compare)
    }

    constructor(deps: TriggerDeps, triggerBg: TriggerBg) : this(deps) {
        bg = InputBg(profileFunction, triggerBg.bg.value, triggerBg.bg.units)
        comparator = Comparator(rh, triggerBg.comparator.value)
    }

    fun setUnits(units: GlucoseUnit): TriggerBg {
        bg.units = units
        return this
    }

    fun setValue(value: Double): TriggerBg {
        bg.value = value
        return this
    }

    fun comparator(comparator: Comparator.Compare): TriggerBg {
        this.comparator.value = comparator
        return this
    }

    override suspend fun shouldRun(): Boolean {
        val glucoseStatus = glucoseStatusProvider.glucoseStatusData
        if (glucoseStatus == null && comparator.value == Comparator.Compare.IS_NOT_AVAILABLE) {
            aapsLogger.debug(LTag.AUTOMATION, "Ready for execution: " + friendlyDescription())
            return true
        }
        if (glucoseStatus == null) {
            aapsLogger.debug(LTag.AUTOMATION, "NOT ready for execution: " + friendlyDescription())
            return false
        }
        if (comparator.value.check(glucoseStatus.glucose.roundToInt(), profileUtil.convertToMgdl(bg.value, bg.units).roundToInt())) {
            aapsLogger.debug(LTag.AUTOMATION, "Ready for execution: " + friendlyDescription())
            return true
        }
        aapsLogger.debug(LTag.AUTOMATION, "NOT ready for execution: " + friendlyDescription())
        return false
    }

    override fun dataJSON(): JsonObject =
        buildJsonObject {
            put("bg", bg.value)
            put("comparator", comparator.value.toString())
            put("units", bg.units.asText)
        }

    override fun fromJSON(data: String): Trigger {
        val d = jsonOf(data)
        bg.setUnits(GlucoseUnit.fromText(d.lenientString("units", GlucoseUnit.MGDL.asText)))
        bg.value = d.lenientDouble("bg")
        comparator.setValue(Comparator.Compare.valueOf(d.lenientStringOrNull("comparator")!!))
        return this
    }

    override fun friendlyName(): TextRef = CoreUiStrings.glucose

    override fun friendlyDescription(): String {
        return if (comparator.value == Comparator.Compare.IS_NOT_AVAILABLE)
            rh.gs(AutomationStrings.glucoseisnotavailable)
        else
            rh.gs(if (bg.units == GlucoseUnit.MGDL) AutomationStrings.glucosecomparedmgdl else AutomationStrings.glucosecomparedmmol, rh.gs(comparator.value.stringRes), bg.value, bg.units)
    }

    override fun composeIcon() = IcBgCheck
    override fun elementType() = ElementType.BG_CHECK

    override fun duplicate(): Trigger = TriggerBg(deps, this)

}
