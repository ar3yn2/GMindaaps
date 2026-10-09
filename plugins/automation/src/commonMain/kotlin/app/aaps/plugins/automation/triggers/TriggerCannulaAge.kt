package app.gmaaps.plugins.automation.triggers

import app.gmaaps.core.data.format.NumberFormat
import app.gmaaps.core.data.model.TE
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.navigation.ElementType
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.core.ui.compose.icons.IcCannulaChange
import app.gmaaps.core.utils.lenientDouble
import app.gmaaps.core.utils.lenientStringOrNull
import app.gmaaps.plugins.automation.AutomationStrings
import app.gmaaps.plugins.automation.elements.Comparator
import app.gmaaps.plugins.automation.elements.InputDouble
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class TriggerCannulaAge(deps: TriggerDeps) : Trigger(deps) {

    var cannulaAgeHours: InputDouble = InputDouble(0.0, 0.0, 336.0, 0.1, NumberFormat.DECIMAL_1)
    var comparator: Comparator = Comparator(rh)

    private constructor(deps: TriggerDeps, triggerCannulaAge: TriggerCannulaAge) : this(deps) {
        cannulaAgeHours = InputDouble(triggerCannulaAge.cannulaAgeHours)
        comparator = Comparator(rh, triggerCannulaAge.comparator.value)
    }

    fun setValue(value: Double): TriggerCannulaAge {
        cannulaAgeHours.value = value
        return this
    }

    fun comparator(comparator: Comparator.Compare): TriggerCannulaAge {
        this.comparator.value = comparator
        return this
    }

    override suspend fun shouldRun(): Boolean {
        val therapyEvent = persistenceLayer.getLastTherapyRecordUpToNow(TE.Type.CANNULA_CHANGE)
        val currentAgeHours = therapyEvent?.timestamp?.let { timestamp ->
            (dateUtil.now() - timestamp) / (60 * 60 * 1000.0)
        } ?: 0.0
        if (therapyEvent == null && comparator.value == Comparator.Compare.IS_NOT_AVAILABLE) {
            aapsLogger.debug(LTag.AUTOMATION, "Ready for execution: " + friendlyDescription())
            return true
        }
        if (therapyEvent == null) {
            aapsLogger.debug(LTag.AUTOMATION, "NOT ready for execution: " + friendlyDescription())
            return false
        }
        if (comparator.value.check(currentAgeHours, cannulaAgeHours.value)) {
            aapsLogger.debug(LTag.AUTOMATION, "Ready for execution: " + friendlyDescription())
            return true
        }
        aapsLogger.debug(LTag.AUTOMATION, "NOT ready for execution: " + friendlyDescription())
        return false
    }

    override fun dataJSON(): JsonObject =
        buildJsonObject {
            put("cannulaAgeHours", cannulaAgeHours.value)
            put("comparator", comparator.value.toString())
        }

    override fun fromJSON(data: String): Trigger {
        val d = jsonOf(data)
        cannulaAgeHours.setValue(d.lenientDouble("cannulaAgeHours"))
        comparator.setValue(Comparator.Compare.valueOf(d.lenientStringOrNull("comparator")!!))
        return this
    }

    override fun friendlyName(): TextRef = AutomationStrings.triggerCannulaAgeLabel

    override fun friendlyDescription(): String =
        rh.gs(AutomationStrings.triggerCannulaAgeDesc, rh.gs(comparator.value.stringRes), cannulaAgeHours.value)

    override fun composeIcon() = IcCannulaChange
    override fun elementType() = ElementType.CANNULA_CHANGE

    override fun duplicate(): Trigger = TriggerCannulaAge(deps, this)

}

