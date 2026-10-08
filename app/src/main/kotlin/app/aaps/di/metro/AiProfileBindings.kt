package app.aaps.di.metro

import app.aaps.core.interfaces.db.PersistenceLayer
import app.aaps.core.interfaces.logging.AAPSLogger
import app.aaps.core.interfaces.notifications.NotificationManager
import app.aaps.core.interfaces.plugin.PluginBase
import app.aaps.core.interfaces.resources.TextResolver
import app.aaps.profile.ai.*
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntKey
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
@BindingContainer
object AiProfileBindings {

    @Provides
    @SingleIn(AppScope::class)
    fun aiConfigStore(
        preferences: app.aaps.core.keys.interfaces.Preferences
    ): AiConfigStore = AiConfigStore(preferences)

    @Provides
    @SingleIn(AppScope::class)
    fun aiHttpClient(
        configStore: AiConfigStore,
        aapsLogger: AAPSLogger
    ): AiApiClient {
        return AiHttpClient(configStore) { msg ->
            aapsLogger.d("AI", "Http", msg)
        }
    }

    @Provides
    @SingleIn(AppScope::class)
    fun aiProfilePlugin(
        persistence: PersistenceLayer,
        configStore: AiConfigStore,
        httpClient: AiApiClient,
        aapsLogger: AAPSLogger,
        rh: TextResolver,
        notificationManager: NotificationManager
    ): PluginBase = AiProfilePlugin(
        pluginDescription = PluginDescription(
            id = 42,
            name = "AI Profile Analyzer",
            tag = "AIProfilePlugin"
        ),
        aapsLogger = aapsLogger,
        rh = rh,
        notificationManager = notificationManager,
        persistence = persistence,
        configStore = configStore,
        aiApiClient = httpClient
    )

    @Provides
    @IntKey(42)
    fun aiProfileEntry(plugin: PluginBase): PluginBase = plugin
}

