package app.gmaaps.desktop.shell.di

import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.L
import app.gmaaps.core.interfaces.notifications.SystemNotificationPlatform
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.sharedPreferences.KeyValueStore
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.desktop.shell.appIconResource
import app.gmaaps.desktop.shell.config.DesktopClientConfig
import app.gmaaps.desktop.shell.loadAwtAppIcon
import app.gmaaps.desktop.shell.platform.DesktopSystemNotificationPlatform
import app.gmaaps.desktop.shell.prefs.DesktopSp
import app.gmaaps.implementation.logging.AAPSLoggerDesktop
import app.gmaaps.shared.impl.utils.DateUtilImpl
import app.gmaaps.shared.impl.utils.JvmDateFormatPlatform
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

/**
 * The bindings the desktop graph needs that only a desktop can supply.
 *
 * The counterpart of what `:app` states on Android and `IosPlatformBindings` on Apple. Everything
 * here is either a real desktop implementation or a shared one being handed its platform piece -
 * nothing in this file is a stand-in.
 *
 * `@ContributesTo(AppScope::class)`, so the graph does not have to include it by name.
 *
 * The database is deliberately **not** here: the graph names its own file, so a future probe graph
 * cannot write into the app's data.
 */
@ContributesTo(AppScope::class)
@BindingContainer
object DesktopPlatformBindings {

    /** Console for now, and a rotating file beside the database for afterwards. */
    @Provides
    @SingleIn(AppScope::class)
    // `L` is deferred: it reads Preferences, which needs a logger. See IosPlatformBindings.
    fun logger(logConfig: () -> L): AAPSLogger = AAPSLoggerDesktop(logConfig = logConfig)

    /** A properties file next to the database, which `PreferencesImpl` sits on unchanged. */
    @Provides
    @SingleIn(AppScope::class)
    fun keyValueStore(): KeyValueStore = DesktopSp()

    @Provides
    @SingleIn(AppScope::class)
    fun dateUtil(): DateUtil = DateUtilImpl(JvmDateFormatPlatform())

    @Provides
    @SingleIn(AppScope::class)
    fun config(): Config = DesktopClientConfig()

    @Provides
    @SingleIn(AppScope::class)
    fun systemNotificationPlatform(logger: AAPSLogger, config: Config, rh: TextResolver): SystemNotificationPlatform =
        DesktopSystemNotificationPlatform(logger, rh.gs(config.appName), loadAwtAppIcon(appIconResource(config)))

}

