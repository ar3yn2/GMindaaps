package app.gmaaps.plugins.source

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.notifications.NotificationManager
import app.gmaaps.core.interfaces.plugin.PluginBaseWithPreferences
import app.gmaaps.core.interfaces.plugin.PluginDescription
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.source.BgSource
import app.gmaaps.core.keys.BooleanKey
import app.gmaaps.core.keys.interfaces.NonPreferenceKey
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.ui.compose.preference.PreferenceSubScreenDef

abstract class AbstractBgSourceWithSensorInsertLogPlugin(
    pluginDescription: PluginDescription,
    ownPreferences: List<NonPreferenceKey> = emptyList(),
    aapsLogger: AAPSLogger,
    rh: TextResolver,
    preferences: Preferences,
    notificationManager: NotificationManager,
) : PluginBaseWithPreferences(pluginDescription, ownPreferences, aapsLogger, rh, preferences, notificationManager), BgSource {

    override fun getPreferenceScreenContent() = PreferenceSubScreenDef(
        key = "bg_source_with_sensor_settings",
        // a BG source plugin always names itself
        title = pluginDescription.pluginName!!,
        items = listOf(
            BooleanKey.BgSourceUploadToNs,
            BooleanKey.BgSourceCreateSensorChange

        ),
        icon = pluginDescription.icon
    )
}

