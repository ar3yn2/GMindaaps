package app.gmaaps.plugins.automation.triggers

import app.gmaaps.core.data.format.NumberFormat
import app.gmaaps.core.data.pump.defs.PumpType
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.navigation.ElementType
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.core.ui.compose.icons.IcPumpBattery
import app.gmaaps.core.utils.lenientDouble
import app.gmaaps.core.utils.lenientStringOrNull
import app.gmaaps.plugins.automation.AutomationStrings
import app.gmaaps.plugins.automation.elements.Comparator
import app.gmaaps.plugins.automation.elements.InputDouble
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class TriggerPumpBatteryLevel(deps: TriggerDeps) : Trigger(deps) {

    var pumpBatteryLevel: InputDouble = InputDouble(0.0, 0.0, 100.0, 1.0, NumberFormat.INTEGER)
    var comparator: Comparator = Comparator(rh)

    private constructor(deps: TriggerDeps, triggerPumpBatteryLevel: TriggerPumpBatteryLevel) : this(deps) {
        pumpBatteryLevel = InputDouble(triggerPumpBatteryLevel.pumpBatteryLevel)
        comparator = Comparator(rh, triggerPumpBatteryLevel.comparator.value)
    }

    fun setValue(value: Double): TriggerPumpBatteryLevel {
        pumpBatteryLevel.value = value
        return this
    }

    fun comparator(comparator: Comparator.Compare): TriggerPumpBatteryLevel {
        this.comparator.value = comparator
        return this
    }

    override suspend fun shouldRun(): Boolean {
        val pump = activePlugin.activePump
        val erosBatteryLinkAvailable = pump.model() == PumpType.OMNIPOD_EROS && pump.isUseRileyLinkBatteryLevel()
        val currentLevel = pump.batteryLevel.value?.toDouble()
        val available = currentLevel != null && (pump.model().supportBatteryLevel || erosBatteryLinkAvailable)
        if (!available) {
            aapsLogger.debug(LTag.AUTOMATION, "NOT ready for execution: " + friendlyDescription())
            return false
        }
        if (comparator.value.check(currentLevel, pumpBatteryLevel.value)) {
            aapsLogger.debug(LTag.AUTOMATION, "Ready for execution: " + friendlyDescription())
            return true
        }
        aapsLogger.debug(LTag.AUTOMATION, "NOT ready for execution: " + friendlyDescription())
        return false
    }

    override fun dataJSON(): JsonObject =
        buildJsonObject {
            put("pumpBatteryLevel", pumpBatteryLevel.value)
            put("comparator", comparator.value.toString())
        }

    override fun fromJSON(data: String): Trigger {
        val d = jsonOf(data)
        pumpBatteryLevel.setValue(d.lenientDouble("pumpBatteryLevel"))
        comparator.setValue(Comparator.Compare.valueOf(d.lenientStringOrNull("comparator")!!))
        return this
    }

    override fun friendlyName(): TextRef = AutomationStrings.triggerPumpBatteryLevelLabel

    override fun friendlyDescription(): String =
        rh.gs(AutomationStrings.triggerPumpBatteryLevelDesc, rh.gs(comparator.value.stringRes), pumpBatteryLevel.value)

    override fun composeIcon() = IcPumpBattery
    override fun elementType() = ElementType.BATTERY_CHANGE

    override fun duplicate(): Trigger = TriggerPumpBatteryLevel(deps, this)

}

