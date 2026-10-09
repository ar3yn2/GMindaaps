package app.gmaaps.di.metro

import app.gmaaps.core.data.plugin.PluginType
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.notifications.NotificationManager
import app.gmaaps.core.interfaces.plugin.PluginBase
import app.gmaaps.core.interfaces.plugin.PluginDescription
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.profile.ai.*
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntKey
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
@BindingContainer
object AiProfileBindings {

    @Provides
    @SingleIn(AppScope::class)
    fun aiConfigStore(
        preferences: Preferences
    ): AiConfigStore = AiConfigStore(preferences)

    @Provides
    @SingleIn(AppScope::class)
    fun aiHttpClient(): AiApiClient {
        return AiHttpClient()
    }

    @Provides
    @SingleIn(AppScope::class)
    fun aiProfilePlugin(
        persistence: PersistenceLayer,
        configStore: AiConfigStore,
        httpClient: AiApiClient,
        aapsLogger: AAPSLogger,
        rh: app.gmaaps.core.interfaces.resources.TextResolver,
        notificationManager: NotificationManager
    ): PluginBase = AiProfilePlugin(
        pluginDescription = PluginDescription().apply {
            mainType = PluginType.GENERAL
            pluginName = TextRef.Literal("AI Profile Analyzer")
            description = TextRef.Literal("Analyzes glucose profile and recommends ISF/basal adjustments based on spike patterns")
        },
        aapsLogger = aapsLogger,
        rh = rh,
        notificationManager = notificationManager,
        persistence = persistence,
        configStore = configStore,
        aiApiClient = httpClient
    )

    @Provides
    @IntoMap
    @IntKey(42)
    fun aiProfileEntry(plugin: PluginBase): PluginBase = plugin
}


