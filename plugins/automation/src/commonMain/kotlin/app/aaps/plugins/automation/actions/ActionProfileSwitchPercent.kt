package app.gmaaps.plugins.automation.actions

import app.gmaaps.core.data.ue.Sources
import app.gmaaps.core.data.ue.ValueWithUnit
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.navigation.ElementType
import app.gmaaps.core.interfaces.profile.ProfileFunction
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.core.ui.CoreUiStrings
import app.gmaaps.core.ui.compose.icons.IcProfile
import app.gmaaps.core.utils.lenientDouble
import app.gmaaps.core.utils.lenientInt
import app.gmaaps.plugins.automation.AutomationStrings
import app.gmaaps.plugins.automation.elements.Comparator
import app.gmaaps.plugins.automation.elements.InputDuration
import app.gmaaps.plugins.automation.elements.InputPercent
import app.gmaaps.plugins.automation.triggers.Trigger
import app.gmaaps.plugins.automation.triggers.TriggerDeps
import app.gmaaps.plugins.automation.triggers.TriggerProfilePercent
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class ActionProfileSwitchPercent(
    aapsLogger: AAPSLogger,
    rh: TextResolver,
    pumpEnactResultProvider: () -> PumpEnactResult,
    private val profileFunction: ProfileFunction,
    // Only to build the Trigger precondition below.
    private val triggerDeps: TriggerDeps
) : Action(aapsLogger, rh, pumpEnactResultProvider) {


    var pct = InputPercent()
    var duration = InputDuration(30, InputDuration.TimeUnit.MINUTES)

    override var precondition: Trigger? = TriggerProfilePercent(triggerDeps, 100.0, Comparator.Compare.IS_EQUAL)

    override fun friendlyName(): TextRef = AutomationStrings.profilepercentage
    override fun shortDescription(): String =
        if (duration.value == 0) rh.gs(AutomationStrings.startprofileforever, pct.value.toInt())
        else rh.gs(CoreUiStrings.startprofile, pct.value.toInt(), duration.value)

    override fun composeIcon() = IcProfile
    override fun elementType() = ElementType.PROFILE_MANAGEMENT

    override suspend fun doAction(): PumpEnactResult {
        val switched = profileFunction.createProfileSwitch(
            durationInMinutes = duration.value,
            percentage = pct.value.toInt(),
            timeShiftInHours = 0,
            action = app.gmaaps.core.data.ue.Action.PROFILE_SWITCH,
            source = Sources.Automation,
            note = title + ": " + rh.gs(CoreUiStrings.startprofile, pct.value.toInt(), duration.value),
            listValues = listOf(
                ValueWithUnit.Percent(pct.value.toInt()),
                ValueWithUnit.Minute(duration.value)
            )
        ) != null
        return if (switched) {
            pumpEnactResultProvider().success(true).comment(CoreUiStrings.ok)
        } else {
            aapsLogger.error(LTag.AUTOMATION, "Final profile not valid")
            pumpEnactResultProvider().success(false).comment(CoreUiStrings.ok)
        }
    }

    override fun hasDialog(): Boolean = true

    override fun toJSON(): String {
        val data = buildJsonObject {
            put("percentage", pct.value)
            put("durationInMinutes", duration.value)
        }
        return buildJsonObject {
            put("type", this@ActionProfileSwitchPercent::class.simpleName)
            put("data", data)
        }.toString()
    }

    override fun fromJSON(data: String): Action {
        val o = jsonOf(data)
        pct.value = o.lenientDouble("percentage")
        duration.value = o.lenientInt("durationInMinutes")
        return this
    }

    override fun isValid(): Boolean =
        pct.value >= InputPercent.MIN &&
            pct.value <= InputPercent.MAX &&
            duration.value > 0
}
