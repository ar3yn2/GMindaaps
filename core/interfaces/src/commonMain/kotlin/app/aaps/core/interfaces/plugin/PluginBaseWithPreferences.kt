package app.gmaaps.core.interfaces.plugin

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.notifications.NotificationManager
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.keys.interfaces.NonPreferenceKey
import app.gmaaps.core.keys.interfaces.Preferences

/**
 * Add preference handling to [PluginBase]
 */
abstract class PluginBaseWithPreferences(
    pluginDescription: PluginDescription,
    val ownPreferences: List<NonPreferenceKey> = emptyList(),
    aapsLogger: AAPSLogger,
    rh: TextResolver,
    val preferences: Preferences,
    notificationManager: NotificationManager
) : PluginBase(pluginDescription, aapsLogger, rh, notificationManager) {

    init {
        preferences.registerPreferences(ownPreferences)
    }

    /**
     * Called before importing preferences.
     * On Preferences import current content of shared preferences is cleared and then
     * new content is imported.
     *
     * This function is called BEFORE clearing and importing preferences to preserve some current values.
     * Needed content should be stored into plugin variables and then processed and restored
     * in [app.gmaaps.core.interfaces.plugin.PluginBaseWithPreferences.afterImport]
     *
     * The app is NOT restarted after an import. This line used to say it was, and that stopped being
     * true: the import now stops the plugins, replaces the preferences and starts them again in the
     * same process, inside the window where
     * [app.gmaaps.core.interfaces.configuration.Config.appInitialized] is false. So whatever is kept
     * here has to survive in the live object, and nothing may rely on getting a fresh process.
     *
     * See: [app.gmaaps.core.interfaces.maintenance.ImportExportPrefs.doImportSharedPreferences]
     */
    open fun beforeImport() {}

    /**
     * Called after importing preferences.
     * On Preferences import current content of shared preferences is cleared and then
     * new content is imported.
     *
     * This function is called AFTER clearing and importing preferences to process and restore
     * saved state from [app.gmaaps.core.interfaces.plugin.PluginBaseWithPreferences.beforeImport]
     *
     * The app is NOT restarted after an import - see
     * [app.gmaaps.core.interfaces.plugin.PluginBaseWithPreferences.beforeImport]. This runs while the
     * app is still reconfiguring, so it must not assume the plugins around it have started yet.
     *
     * See: [app.gmaaps.core.interfaces.maintenance.ImportExportPrefs.doImportSharedPreferences]
     */
    open fun afterImport() {}
}
