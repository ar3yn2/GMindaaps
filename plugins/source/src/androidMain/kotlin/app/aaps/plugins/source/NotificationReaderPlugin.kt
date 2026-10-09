package app.gmaaps.plugins.source

import android.content.Context
import app.gmaaps.core.data.plugin.PluginType
import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.notifications.NotificationManager
import app.gmaaps.core.interfaces.plugin.PermissionGroup
import app.gmaaps.core.interfaces.plugin.PluginBase
import app.gmaaps.core.interfaces.plugin.PluginDescription
import app.gmaaps.core.interfaces.resources.ResourceHelper
import app.gmaaps.core.interfaces.source.BgSource
import app.gmaaps.core.keys.StringNonKey
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.core.ui.compose.icons.IcPluginByoda
import app.gmaaps.plugins.source.compose.BgSourceComposeContent
import app.gmaaps.plugins.source.notificationreader.PackageConfig
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.IntKey
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.binding
import java.net.URL

@ContributesIntoMap(AppScope::class, binding = binding<PluginBase>())
@IntKey(530)
@SingleIn(AppScope::class)
@Inject
class NotificationReaderPlugin(
    rh: ResourceHelper,
    aapsLogger: AAPSLogger,
    preferences: Preferences,
    config: Config,
    private val context: Context,
    notificationManager: NotificationManager,
) : AbstractBgSourcePlugin(
    pluginDescription = PluginDescription()
        .mainType(PluginType.BGSOURCE)
        .composeContent { _ ->
            BgSourceComposeContent(
                title = rh.gs(R.string.notification_reader)
            )
        }
        .icon(IcPluginByoda)
        .pluginName(TextRef.AndroidRes(R.string.notification_reader))
        .preferencesVisibleInSimpleMode(false)
        .description(TextRef.AndroidRes(R.string.description_source_notification_reader)),
    ownPreferences = emptyList(),
    aapsLogger, rh, preferences, config, notificationManager
), BgSource {

    @Volatile var packageConfig: PackageConfig = loadPackageConfig()
        private set

    override suspend fun onStart() {
        super.onStart()
        Thread {
            updateDefinitionsFromRemote()
        }.start()
    }

    override fun requiredPermissions(): List<PermissionGroup> = listOf(
        PermissionGroup(
            permissions = listOf(PERMISSION_NOTIFICATION_LISTENER),
            rationaleTitle = TextRef.AndroidRes(R.string.permission_notification_listener_title),
            rationaleDescription = TextRef.AndroidRes(R.string.permission_notification_listener_description),
            special = true,
        )
    )

    private fun loadPackageConfig(): PackageConfig {
        return try {
            val stored = preferences.get(StringNonKey.NotificationReaderPackages)
            val json = if (stored.isNotEmpty()) {
                stored
            } else {
                val asset = context.assets.open(PACKAGE_CONFIG_ASSET).bufferedReader().use { it.readText() }
                preferences.put(StringNonKey.NotificationReaderPackages, asset)
                asset
            }
            PackageConfig.fromJson(json)
        } catch (e: Exception) {
            aapsLogger.error(LTag.BGSOURCE, "Failed to load package config", e)
            PackageConfig(0, emptySet(), emptyMap(), emptyMap())
        }
    }

    private fun updateDefinitionsFromRemote() {
        try {
            val remoteJson = URL(REMOTE_DEFINITIONS_URL).readText()
            val remoteConfig = PackageConfig.fromJson(remoteJson)
            if (remoteConfig.version > packageConfig.version) {
                aapsLogger.info(LTag.BGSOURCE, "Updating notification reader definitions: v${packageConfig.version} → v${remoteConfig.version}")
                preferences.put(StringNonKey.NotificationReaderPackages, remoteJson)
                packageConfig = remoteConfig
            } else {
                aapsLogger.debug(LTag.BGSOURCE, "Notification reader definitions up to date (v${packageConfig.version})")
            }
        } catch (e: Exception) {
            aapsLogger.debug(LTag.BGSOURCE, "Failed to fetch remote definitions: ${e.message}")
        }
    }

    companion object {

        const val PERMISSION_NOTIFICATION_LISTENER = "app.gmaaps.permission.NOTIFICATION_LISTENER"
        private const val PACKAGE_CONFIG_ASSET = "notification_reader_packages.json"
        private const val REMOTE_DEFINITIONS_URL = "https://raw.githubusercontent.com/nightscout/AndroidAPS/refs/heads/versions/notification_reader_packages.json"
    }
}

