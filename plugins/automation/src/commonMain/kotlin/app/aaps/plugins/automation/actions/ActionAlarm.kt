package app.gmaaps.plugins.automation.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import app.gmaaps.core.interfaces.alerts.ReminderScheduler
import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.navigation.ElementType
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.core.ui.CoreUiStrings
import app.gmaaps.core.utils.lenientString
import app.gmaaps.plugins.automation.AutomationStrings
import app.gmaaps.plugins.automation.elements.InputString
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class ActionAlarm(
    aapsLogger: AAPSLogger,
    rh: TextResolver,
    pumpEnactResultProvider: () -> PumpEnactResult,
    private val rxBus: RxBus,
    private val dateUtil: DateUtil,
    private val reminderScheduler: ReminderScheduler,
    private val config: Config
) : Action(aapsLogger, rh, pumpEnactResultProvider) {


    var text = InputString()

    constructor(
        aapsLogger: AAPSLogger,
        rh: TextResolver,
        pumpEnactResultProvider: () -> PumpEnactResult,
        rxBus: RxBus,
        dateUtil: DateUtil,
        reminderScheduler: ReminderScheduler,
        config: Config,
        text: String
    ) : this(aapsLogger, rh, pumpEnactResultProvider, rxBus, dateUtil, reminderScheduler, config) {
        this.text = InputString(text)
    }

    override fun friendlyName(): TextRef = CoreUiStrings.alarm
    override fun shortDescription(): String = rh.gs(AutomationStrings.alarm_message, text.value)
    override fun composeIcon() = Icons.Filled.Alarm
    override fun elementType() = ElementType.BG_CHECK

    override fun isValid(): Boolean = true // empty alarm will show app name

    override suspend fun doAction(): PumpEnactResult {
        reminderScheduler.scheduleReminder(10, text.value.takeIf { it.isNotBlank() }
            ?: rh.gs(config.appName))
        return pumpEnactResultProvider().success(true).comment(CoreUiStrings.ok)
    }

    override fun toJSON(): String =
        buildJsonObject {
            put("type", this@ActionAlarm::class.simpleName)
            put("data", buildJsonObject { put("text", text.value) })
        }.toString()

    override fun fromJSON(data: String): Action {
        val o = jsonOf(data)
        text.value = o.lenientString("text", "")
        return this
    }

    override fun hasDialog(): Boolean = true

}
