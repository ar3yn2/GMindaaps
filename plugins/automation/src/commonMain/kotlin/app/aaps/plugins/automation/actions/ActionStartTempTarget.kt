package app.gmaaps.plugins.automation.actions

import app.gmaaps.core.data.configuration.Constants
import app.gmaaps.core.data.model.GlucoseUnit
import app.gmaaps.core.data.model.TT
import app.gmaaps.core.data.ue.Sources
import app.gmaaps.core.data.ue.ValueWithUnit
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.navigation.ElementType
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.profile.ProfileFunction
import app.gmaaps.core.interfaces.profile.ProfileUtil
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.core.ui.CoreUiStrings
import app.gmaaps.core.ui.compose.icons.IcTtHigh
import app.gmaaps.core.ui.extensions.friendlyDescription
import app.gmaaps.core.utils.lenientDouble
import app.gmaaps.core.utils.lenientInt
import app.gmaaps.core.utils.lenientString
import app.gmaaps.plugins.automation.AutomationStrings
import app.gmaaps.plugins.automation.elements.ComparatorExists
import app.gmaaps.plugins.automation.elements.InputDuration
import app.gmaaps.plugins.automation.elements.InputTempTarget
import app.gmaaps.plugins.automation.triggers.Trigger
import app.gmaaps.plugins.automation.triggers.TriggerDeps
import app.gmaaps.plugins.automation.triggers.TriggerTempTarget
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

class ActionStartTempTarget(
    aapsLogger: AAPSLogger,
    rh: TextResolver,
    pumpEnactResultProvider: () -> PumpEnactResult,
    private val activePlugin: ActivePlugin,
    private val persistenceLayer: PersistenceLayer,
    private val profileFunction: ProfileFunction,
    private val dateUtil: DateUtil,
    private val profileUtil: ProfileUtil,
    // Only to build the Trigger precondition below.
    private val triggerDeps: TriggerDeps
) : Action(aapsLogger, rh, pumpEnactResultProvider) {


    var value = InputTempTarget(profileFunction)
    var duration = InputDuration(30, InputDuration.TimeUnit.MINUTES)

    override var precondition: Trigger? = TriggerTempTarget(triggerDeps, ComparatorExists.Compare.NOT_EXISTS)

    override fun friendlyName(): TextRef = AutomationStrings.starttemptarget
    override fun shortDescription(): String = rh.gs(AutomationStrings.starttemptarget) + ": " + tt().friendlyDescription(value.units, rh, profileUtil)
    override fun composeIcon() = IcTtHigh
    override fun elementType() = ElementType.TEMP_TARGET_MANAGEMENT

    override suspend fun doAction(): PumpEnactResult =
        try {
            persistenceLayer.insertAndCancelCurrentTemporaryTarget(
                temporaryTarget = tt(), action = app.gmaaps.core.data.ue.Action.TT,
                source = Sources.Automation,
                note = title,
                listValues = listOfNotNull(
                    ValueWithUnit.TETTReason(TT.Reason.AUTOMATION),
                    ValueWithUnit.Mgdl(tt().lowTarget),
                    ValueWithUnit.Mgdl(tt().highTarget).takeIf { tt().lowTarget != tt().highTarget },
                    ValueWithUnit.Minute(tt().duration.milliseconds.inWholeMinutes.toInt())
                )
            )
            pumpEnactResultProvider().success(true).comment(CoreUiStrings.ok)
        } catch (_: Exception) {
            pumpEnactResultProvider().success(false).comment(CoreUiStrings.error)
        }

    override fun hasDialog(): Boolean {
        return true
    }

    override fun toJSON(): String {
        val data = buildJsonObject {
            put("value", value.value)
            put("units", value.units.asText)
            put("durationInMinutes", duration.getMinutes())
        }
        return buildJsonObject {
            put("type", this@ActionStartTempTarget::class.simpleName)
            put("data", data)
        }.toString()
    }

    override fun fromJSON(data: String): Action {
        val o = jsonOf(data)
        value.units = GlucoseUnit.fromText(o.lenientString("units", GlucoseUnit.MGDL.asText))
        value.value = o.lenientDouble("value")
        duration.setMinutes(o.lenientInt("durationInMinutes"))
        return this
    }

    private fun tt() = TT(
        timestamp = dateUtil.now(),
        duration = duration.getMinutes().toLong().minutes.inWholeMilliseconds,
        reason = TT.Reason.AUTOMATION,
        lowTarget = profileUtil.convertToMgdl(value.value, value.units),
        highTarget = profileUtil.convertToMgdl(value.value, value.units)
    )

    override fun isValid(): Boolean =
        if (value.units == GlucoseUnit.MMOL) { // mmol
            value.value in Constants.TT_RANGE_MMOL && duration.value > 0
        } else { // mg/dL
            value.value in Constants.TT_RANGE_MGDL && duration.value > 0
        }
}
