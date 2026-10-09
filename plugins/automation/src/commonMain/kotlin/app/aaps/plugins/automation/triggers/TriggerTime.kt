package app.gmaaps.plugins.automation.triggers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import app.gmaaps.core.data.time.T
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.navigation.ElementType
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.core.ui.CoreUiStrings
import app.gmaaps.core.utils.lenientLong
import app.gmaaps.plugins.automation.AutomationStrings
import app.gmaaps.plugins.automation.elements.InputDateTime
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class TriggerTime(deps: TriggerDeps) : Trigger(deps) {

    var time = InputDateTime(rh, dateUtil)

    constructor(deps: TriggerDeps, runAt: Long) : this(deps) {
        this.time.value = runAt
    }

    @Suppress("unused")
    constructor(deps: TriggerDeps, triggerTime: TriggerTime) : this(deps) {
        this.time.value = triggerTime.time.value
    }

    fun runAt(time: Long): TriggerTime {
        this.time.value = time
        return this
    }

    override suspend fun shouldRun(): Boolean {
        val now = dateUtil.now()
        if (now >= time.value && now - time.value < T.mins(5).msecs()) {
            aapsLogger.debug(LTag.AUTOMATION, "Ready for execution: " + friendlyDescription())
            return true
        }
        aapsLogger.debug(LTag.AUTOMATION, "NOT ready for execution: " + friendlyDescription())
        return false
    }

    override fun dataJSON(): JsonObject =
        buildJsonObject {
            put("runAt", time.value)
        }

    override fun fromJSON(data: String): Trigger {
        val o = jsonOf(data)
        time.value = o.lenientLong("runAt")
        return this
    }

    override fun friendlyName(): TextRef = CoreUiStrings.time

    override fun friendlyDescription(): String =
        rh.gs(AutomationStrings.atspecifiedtime, dateUtil.dateAndTimeString(time.value))

    override fun composeIcon() = Icons.Filled.Schedule
    override fun elementType() = ElementType.AUTOMATION

    override fun duplicate(): Trigger = TriggerTime(deps, time.value)

}
