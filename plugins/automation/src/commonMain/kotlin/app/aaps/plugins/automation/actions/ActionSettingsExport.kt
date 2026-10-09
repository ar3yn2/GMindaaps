package app.gmaaps.plugins.automation.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import app.gmaaps.core.data.model.TE
import app.gmaaps.core.data.ue.Sources
import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.maintenance.ImportExportPrefs
import app.gmaaps.core.interfaces.navigation.ElementType
import app.gmaaps.core.interfaces.notifications.NotificationId
import app.gmaaps.core.interfaces.notifications.NotificationLevel
import app.gmaaps.core.interfaces.notifications.NotificationManager
import app.gmaaps.core.interfaces.protection.ExportPasswordDataStore
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.interfaces.rx.events.EventRefreshOverview
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.keys.BooleanKey
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.core.objects.extensions.asAnnouncement
import app.gmaaps.core.objects.extensions.asSettingsExport
import app.gmaaps.core.ui.CoreUiStrings
import app.gmaaps.core.utils.lenientString
import app.gmaaps.plugins.automation.AutomationStrings
import app.gmaaps.plugins.automation.elements.InputString
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class ActionSettingsExport(
    aapsLogger: AAPSLogger,
    rh: TextResolver,
    pumpEnactResultProvider: () -> PumpEnactResult,
    private val rxBus: RxBus,
    private val notificationManager: NotificationManager,
    private val dateUtil: DateUtil,
    private val config: Config,
    private val persistenceLayer: PersistenceLayer,
    private val importExportPrefs: ImportExportPrefs,
    private val exportPasswordDataStore: ExportPasswordDataStore,
    private val preferences: Preferences
) : Action(aapsLogger, rh, pumpEnactResultProvider) {


    // Not private: the editor binds to it directly, rather than reaching it by reflection.
    val text = InputString()

    override fun friendlyName(): TextRef = CoreUiStrings.exportsettings
    override fun shortDescription(): String = rh.gs(AutomationStrings.exportsettings_message, text.value)
    override fun composeIcon() = Icons.Filled.FileDownload
    override fun elementType() = ElementType.SETTINGS

    override fun isValid(): Boolean = true

    override suspend fun doAction(): PumpEnactResult {

        // Feedback on result
        var exportResultMessage: String
        var exportResultComment: TextRef        // Comment text set in code
        var exportResultLevel: NotificationLevel // Level for user notification when done
        var announceAlert = false      // Also post an announcement (NS)

        if (exportPasswordDataStore.exportPasswordStoreEnabled()) {

            // Get the (encrypted) password and status from the DataStore
            val (password, isExpired, isAboutToExpire) = exportPasswordDataStore.getPasswordFromDataStore()
            aapsLogger.debug(LTag.AUTOMATION, "Exporting settings: passwordIsNotEmpty=${password.isNotEmpty()}, isExpired=$isExpired, isAboutToExpire=$isAboutToExpire")

            // And do according to password state
            if (password.isNotEmpty() && !isExpired) { // Password is not empty and not isExpired
                // Password is not empty and not expired
                if (isAboutToExpire) {
                    // Password is about to expire and needs re-entering by user soon: notify user
                    // Note: we are allowed to export!
                    exportResultComment = CoreUiStrings.export_warning
                    exportResultMessage = rh.gs(CoreUiStrings.export_result_message_about_to_expire)
                    exportResultLevel = NotificationLevel.LOW  // LOW -> e.g. color ORANGE
                } else {
                    // We have a valid password: start exporting, then notify
                    exportResultComment = CoreUiStrings.export_ok
                    exportResultMessage = rh.gs(CoreUiStrings.export_result_message_exported)
                    exportResultLevel = NotificationLevel.INFO // INFO -> e.g. color GREEN
                }
                // Execute settings export, then notify user
                if (!importExportPrefs.exportSharedPreferencesNonInteractive(password)) {
                    // :-( Export failed (see logfile!?)
                    aapsLogger.error(LTag.AUTOMATION, "ERROR: exportSharedPreferencesNonInteractive() failed to export settings")
                    exportResultComment = CoreUiStrings.export_failed
                    exportResultMessage = rh.gs(CoreUiStrings.export_result_message_failed)
                    exportResultLevel = NotificationLevel.IMPORTANT // URGENT -> e.g. color RED
                    announceAlert = true
                }
            } else {
                // No password or was expired and needs re-entering by user
                exportResultComment = CoreUiStrings.export_expired
                exportResultMessage = rh.gs(CoreUiStrings.export_result_message_expired)
                exportResultLevel = NotificationLevel.IMPORTANT  // URGENT -> e.g. color RED
                // Clear password in datastore, then notify user
                aapsLogger.info(LTag.AUTOMATION, "No password or was expired and needs re-entering by user")
                exportPasswordDataStore.clearPasswordDataStore()
                announceAlert = true
            }
        } else {
            // Not enabled, do nothing and notify user
            exportResultComment = CoreUiStrings.export_disabled
            exportResultMessage = rh.gs(CoreUiStrings.export_result_message_disabled)
            exportResultLevel = NotificationLevel.IMPORTANT
            aapsLogger.info(LTag.AUTOMATION, "Settings export ignored: unattended settings export is disabled")
        }
        // send notification
        notificationManager.post(NotificationId.SETTINGS_EXPORT_RESULT, exportResultMessage, exportResultLevel)

        // Insert therapy event EXPORT_SETTINGS for automation trigger to uniquely detect.
        val error = "${text.value}: $exportResultMessage"
        aapsLogger.debug(LTag.AUTOMATION, "Insert therapy EXPORT_SETTINGS event, error=:${error}, doAlsoAnnouncement=$announceAlert")
        persistenceLayer.insertPumpTherapyEventIfNewByTimestamp(
            therapyEvent = TE.asSettingsExport(error = error),
            timestamp = dateUtil.now(),
            action = app.gmaaps.core.data.ue.Action.EXPORT_SETTINGS, // Signal export was done to automation!
            source = Sources.Automation,
            note = exportResultMessage,
            listValues = listOf()
        )

        if (announceAlert && preferences.get(BooleanKey.NsClientCreateAnnouncementsFromErrors) && config.APS) {
            // Do additional event type announcement for aapsClient alerting
            val alert = "${rh.gs(CoreUiStrings.export_alert)}(${text.value}): $exportResultMessage"
            aapsLogger.debug(LTag.AUTOMATION, "Insert therapy ALERT/ANNOUNCEMENT event, error=:${alert}")
            persistenceLayer.insertPumpTherapyEventIfNewByTimestamp(
                therapyEvent = TE.asAnnouncement(error = alert),
                timestamp = dateUtil.now(),
                action = app.gmaaps.core.data.ue.Action.EXPORT_SETTINGS,
                source = Sources.Automation,
                note = exportResultMessage,
                listValues = listOf()
            )
        }

        rxBus.send(EventRefreshOverview("ActionSettingsExport"))
        return pumpEnactResultProvider().success(true).comment(exportResultComment)
    }

    override fun toJSON(): String {
        val data = buildJsonObject { put("text", text.value) }
        return buildJsonObject {
            put("type", this@ActionSettingsExport::class.simpleName)
            put("data", data)
        }.toString()
    }

    override fun fromJSON(data: String): Action {
        val o = jsonOf(data)
        text.value = o.lenientString("text", "")
        return this
    }

    override fun hasDialog(): Boolean = true

}
