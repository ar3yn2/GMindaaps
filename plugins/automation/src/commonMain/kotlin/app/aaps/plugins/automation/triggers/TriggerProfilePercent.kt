package app.gmaaps.plugins.automation.triggers

import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.navigation.ElementType
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.core.objects.profile.ProfileSealed
import app.gmaaps.core.ui.compose.icons.IcProfile
import app.gmaaps.core.utils.lenientDouble
import app.gmaaps.core.utils.lenientStringOrNull
import app.gmaaps.plugins.automation.AutomationStrings
import app.gmaaps.plugins.automation.elements.Comparator
import app.gmaaps.plugins.automation.elements.InputPercent
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.math.roundToInt

class TriggerProfilePercent(deps: TriggerDeps) : Trigger(deps) {

    var pct = InputPercent()
    var comparator = Comparator(rh)

    constructor(deps: TriggerDeps, value: Double, compare: Comparator.Compare) : this(deps) {
        pct = InputPercent(value)
        comparator = Comparator(rh, compare)
    }

    constructor(deps: TriggerDeps, triggerProfilePercent: TriggerProfilePercent) : this(deps) {
        pct = InputPercent(triggerProfilePercent.pct.value)
        comparator = Comparator(rh, triggerProfilePercent.comparator.value)
    }

    fun setValue(value: Double): TriggerProfilePercent {
        this.pct.value = value
        return this
    }

    fun comparator(comparator: Comparator.Compare): TriggerProfilePercent {
        this.comparator.value = comparator
        return this
    }

    override suspend fun shouldRun(): Boolean {
        val profile = profileFunction.getProfile()
        if (profileFunction.isProfileChangePending()) {
            aapsLogger.debug(LTag.AUTOMATION, "NOT ready for execution: " + "Profile change is already pending: " + friendlyDescription())
            return false
        }
        if (profile == null && comparator.value == Comparator.Compare.IS_NOT_AVAILABLE) {
            aapsLogger.debug(LTag.AUTOMATION, "Ready for execution: " + friendlyDescription())
            return true
        }
        if (profile == null) {
            aapsLogger.debug(LTag.AUTOMATION, "NOT ready for execution: " + friendlyDescription())
            return false
        }
        if (profile is ProfileSealed.EPS) {
            if (comparator.value.check(profile.value.originalPercentage, pct.value.roundToInt())) {
                aapsLogger.debug(LTag.AUTOMATION, "Ready for execution: " + friendlyDescription())
                return true
            }
        }
        aapsLogger.debug(LTag.AUTOMATION, "NOT ready for execution: " + friendlyDescription())
        return false
    }

    override fun dataJSON(): JsonObject =
        buildJsonObject {
            put("percentage", pct.value)
            put("comparator", comparator.value.toString())
        }

    override fun fromJSON(data: String): Trigger {
        val d = jsonOf(data)
        pct.value = d.lenientDouble("percentage")
        comparator.setValue(Comparator.Compare.valueOf(d.lenientStringOrNull("comparator")!!))
        return this
    }

    override fun friendlyName(): TextRef = AutomationStrings.profilepercentage

    override fun friendlyDescription(): String =
        rh.gs(AutomationStrings.percentagecompared, rh.gs(comparator.value.stringRes), pct.value.toInt())

    override fun composeIcon() = IcProfile
    override fun elementType() = ElementType.PROFILE_MANAGEMENT

    override fun duplicate(): Trigger = TriggerProfilePercent(deps, this)

}
