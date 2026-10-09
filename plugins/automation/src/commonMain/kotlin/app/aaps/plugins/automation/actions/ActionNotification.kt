package app.gmaaps.plugins.automation.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import app.gmaaps.core.data.model.TE
import app.gmaaps.core.data.ue.Sources
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.navigation.ElementType
import app.gmaaps.core.interfaces.notifications.NotificationId
import app.gmaaps.core.interfaces.notifications.NotificationManager
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.interfaces.rx.events.EventRefreshOverview
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.core.objects.extensions.asAnnouncement
import app.gmaaps.core.ui.CoreUiStrings
import app.gmaaps.core.utils.lenientString
import app.gmaaps.plugins.automation.AutomationStrings
import app.gmaaps.plugins.automation.elements.InputString
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class ActionNotification(
    aapsLogger: AAPSLogger,
    rh: TextResolver,
    pumpEnactResultProvider: () -> PumpEnactResult,
    private val rxBus: RxBus,
    private val notificationManager: NotificationManager,
    private val persistenceLayer: PersistenceLayer,
    private val dateUtil: DateUtil
) : Action(aapsLogger, rh, pumpEnactResultProvider) {


    var text = InputString()

    override fun friendlyName(): TextRef = CoreUiStrings.notification
    override fun shortDescription(): String = rh.gs(AutomationStrings.notification_message, text.value)
    override fun composeIcon() = Icons.Filled.Notifications
    override fun elementType() = ElementType.ANNOUNCEMENT

    override suspend fun doAction(): PumpEnactResult {
        notificationManager.post(NotificationId.AUTOMATION_MESSAGE, text.value)
        persistenceLayer.insertPumpTherapyEventIfNewByTimestamp(
            therapyEvent = TE.asAnnouncement(text.value),
            timestamp = dateUtil.now(),
            action = app.gmaaps.core.data.ue.Action.TREATMENT,
            source = Sources.Automation,
            note = text.value,
            listValues = listOf()
        )
        rxBus.send(EventRefreshOverview("ActionNotification"))
        return pumpEnactResultProvider().success(true).comment(CoreUiStrings.ok)
    }

    override fun toJSON(): String =
        buildJsonObject {
            put("type", this@ActionNotification::class.simpleName)
            put("data", buildJsonObject { put("text", text.value) })
        }.toString()

    override fun fromJSON(data: String): Action {
        val o = jsonOf(data)
        text.value = o.lenientString("text", "")
        return this
    }

    override fun hasDialog(): Boolean = true

    override fun isValid(): Boolean = text.value.isNotEmpty()
}
