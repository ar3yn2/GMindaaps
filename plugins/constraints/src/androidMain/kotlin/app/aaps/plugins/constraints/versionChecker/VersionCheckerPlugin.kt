package app.gmaaps.plugins.constraints.versionChecker

import app.gmaaps.core.data.plugin.PluginType
import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.constraints.Constraint
import app.gmaaps.core.interfaces.constraints.PluginConstraints
import app.gmaaps.core.interfaces.di.NotNSClient
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.notifications.NotificationManager
import app.gmaaps.core.interfaces.plugin.EnforcedState
import app.gmaaps.core.interfaces.plugin.PluginBase
import app.gmaaps.core.interfaces.plugin.PluginBaseWithPreferences
import app.gmaaps.core.interfaces.plugin.PluginDescription
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.interfaces.versionChecker.VersionCheckerUtils
import app.gmaaps.core.keys.LongComposedKey
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.plugins.constraints.ConstraintsStrings
import app.gmaaps.plugins.constraints.versionChecker.keys.VersionCheckerLongKey
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.IntKey
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.binding

@ContributesIntoMap(AppScope::class, binding = binding<PluginBase>())
@NotNSClient
@IntKey(810)
@SingleIn(AppScope::class)
@Inject
class VersionCheckerPlugin(
    aapsLogger: AAPSLogger,
    override val rh: TextResolver,
    preferences: Preferences,
    private val versionCheckerUtils: VersionCheckerUtils,
    private val config: Config,
    private val dateUtil: DateUtil,
    notificationManager: NotificationManager
) : PluginBaseWithPreferences(
    pluginDescription = PluginDescription()
        .mainType(PluginType.CONSTRAINTS)
        .enforce(EnforcedState.Enabled)
        .showInList { false }
        .pluginName(ConstraintsStrings.version_checker),
    ownPreferences = VersionCheckerLongKey.entries,
    aapsLogger, rh, preferences, notificationManager
), PluginConstraints {

    override suspend fun applyMaxIOBConstraints(maxIob: Constraint<Double>): Constraint<Double> {
        versionCheckerUtils.triggerCheckVersion()
        val endDate = preferences.get(LongComposedKey.AppExpiration, config.VERSION_NAME)
        return if (endDate != 0L && dateUtil.now() > endDate)
            maxIob.set(0.0, rh.gs(ConstraintsStrings.application_expired), this)
        else
            maxIob
    }
}

