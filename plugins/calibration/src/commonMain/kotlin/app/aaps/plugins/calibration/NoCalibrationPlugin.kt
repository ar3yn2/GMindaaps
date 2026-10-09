package app.gmaaps.plugins.calibration

import app.gmaaps.core.data.iob.InMemoryGlucoseValue
import app.gmaaps.core.data.plugin.PluginType
import app.gmaaps.core.interfaces.calibration.AddEntryResult
import app.gmaaps.core.interfaces.calibration.Calibration
import app.gmaaps.core.interfaces.calibration.CalibrationContext
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.notifications.NotificationManager
import app.gmaaps.core.interfaces.plugin.PluginBase
import app.gmaaps.core.interfaces.plugin.PluginDescription
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.ui.compose.icons.IcCalibration
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.IntKey
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.binding

@Inject
@SingleIn(AppScope::class)
// Bound as PluginBase, not implicitly: the class has more than one supertype, so Metro cannot pick
// one. The plugin list wants PluginBase.
@ContributesIntoMap(AppScope::class, binding = binding<PluginBase>())
@IntKey(700)
class NoCalibrationPlugin(
    aapsLogger: AAPSLogger,
    rh: TextResolver,
    notificationManager: NotificationManager
) : PluginBase(
    PluginDescription()
        .mainType(PluginType.CALIBRATION)
        .icon(IcCalibration)
        .setDefault(true)
        .pluginName(CalibrationStrings.no_calibration_name)
        .description(CalibrationStrings.description_no_calibration),
    aapsLogger, rh, notificationManager
), Calibration {

    override suspend fun calibrate(
        data: MutableList<InMemoryGlucoseValue>,
        @Suppress("UNUSED_PARAMETER") context: CalibrationContext
    ): MutableList<InMemoryGlucoseValue> = data

    override suspend fun addEntry(bgMgdl: Double, timestamp: Long): AddEntryResult = AddEntryResult.Accepted

    override suspend fun checkPreconditions(): AddEntryResult = AddEntryResult.Accepted
}

