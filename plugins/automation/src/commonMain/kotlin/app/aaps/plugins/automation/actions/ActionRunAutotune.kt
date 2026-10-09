package app.gmaaps.plugins.automation.actions

import app.gmaaps.core.interfaces.autotune.Autotune
import app.gmaaps.core.interfaces.concurrent.aapsIoDispatcher
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.navigation.ElementType
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.profile.ProfileFunction
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.keys.BooleanKey
import app.gmaaps.core.keys.IntKey
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.core.ui.CoreUiStrings
import app.gmaaps.core.ui.compose.icons.IcPluginAutotune
import app.gmaaps.core.ui.elements.WeekDay
import app.gmaaps.core.utils.lenientBoolean
import app.gmaaps.core.utils.lenientInt
import app.gmaaps.core.utils.lenientString
import app.gmaaps.plugins.automation.AutomationStrings
import app.gmaaps.plugins.automation.elements.InputDuration
import app.gmaaps.plugins.automation.elements.InputProfileName
import app.gmaaps.plugins.automation.elements.InputWeekDay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class ActionRunAutotune(
    aapsLogger: AAPSLogger,
    rh: TextResolver,
    pumpEnactResultProvider: () -> PumpEnactResult,
    private val resourceHelper: TextResolver,
    private val autotunePlugin: Autotune,
    private val profileFunction: ProfileFunction,
    private val activePlugin: ActivePlugin,
    private val preferences: Preferences
) : Action(aapsLogger, rh, pumpEnactResultProvider) {


    private var defaultValue = 0
    // Not private: the editor binds to these directly, rather than reaching them by reflection.
    var inputProfileName = InputProfileName("")
    var daysBack = InputDuration(0, InputDuration.TimeUnit.DAYS)
    val days = InputWeekDay().also { it.setAll(true) }

    override fun friendlyName(): TextRef = AutomationStrings.autotune_run
    override fun shortDescription(): String = resourceHelper.gs(AutomationStrings.autotune_profile_name, inputProfileName.value)
    override fun composeIcon() = IcPluginAutotune
    override fun elementType() = ElementType.PROFILE_MANAGEMENT

    override suspend fun doAction(): PumpEnactResult {
        val autoSwitch = preferences.get(BooleanKey.AutotuneAutoSwitchProfile)
        val profileName = if (inputProfileName.value == rh.gs(CoreUiStrings.active)) "" else inputProfileName.value
        var message = if (autoSwitch) AutomationStrings.autotune_run_with_autoswitch else AutomationStrings.autotune_run_without_autoswitch
        return if (!autotunePlugin.calculationRunning) {
            autotunePlugin.atLog("[Automation] Run Autotune $profileName, ${daysBack.value} days, Autoswitch $autoSwitch")
            // aapsAutotune is suspend; runs heavy work but uses suspend I/O internally — keep
            // the explicit IO dispatcher to push the CPU+I/O loop off the caller's dispatcher.
            withContext(aapsIoDispatcher) {
                autotunePlugin.aapsAutotune(daysBack.value, autoSwitch, profileName, days.weekdays)
            }
            if (!autotunePlugin.lastRunSuccess) {
                message = AutomationStrings.autotune_run_with_error
                aapsLogger.error(LTag.AUTOMATION, "Error during Autotune Run")
            }
            pumpEnactResultProvider().success(autotunePlugin.lastRunSuccess).comment(message)
        } else {
            message = AutomationStrings.autotune_run_cancelled
            aapsLogger.debug(LTag.AUTOMATION, "Autotune run detected, Autotune Run Cancelled")
            pumpEnactResultProvider().success(false).comment(message)
        }
    }

    override fun hasDialog(): Boolean = true

    override fun toJSON(): String {
        val data = buildJsonObject {
            put("profileToTune", inputProfileName.value)
            put("tunedays", daysBack.value)
            for (i in days.weekdays.indices) {
                put(WeekDay.DayOfWeek.entries[i].name, days.weekdays[i])
            }
        }
        return buildJsonObject {
            put("type", this@ActionRunAutotune::class.simpleName)
            put("data", data)
        }.toString()
    }

    override fun fromJSON(data: String): Action {
        val o = jsonOf(data)
        for (i in days.weekdays.indices)
            days.weekdays[i] = o.lenientBoolean(WeekDay.DayOfWeek.entries[i].name, true)
        inputProfileName.value = o.lenientString("profileToTune", "")
        defaultValue = o.lenientInt("tunedays")
        if (defaultValue == 0)
            defaultValue = preferences.get(IntKey.AutotuneDefaultTuneDays)
        daysBack.value = defaultValue
        return this
    }

    /**
     * False rather than a crash where autotune does not exist.
     *
     * `getSpecificPluginsListByInterface` searches the plugin *list*, and `AutotunePlugin` is Android
     * only - so the list is empty on the desktop and iOS clients and `first()` threw. The `Autotune`
     * *binding* does exist there, which is what hid this: DI says the feature is present, the plugin
     * list says it is not. Editing rules is supported on a client even though running them is not, so
     * this is reached by simply opening the automation screen. `AllPreferencesScreen` already asks the
     * same question this way.
     */
    override fun isValid(): Boolean = runBlocking { profileFunction.getProfile() } != null &&
        activePlugin.getSpecificPluginsListByInterface(Autotune::class).firstOrNull()?.isEnabled() == true
}
