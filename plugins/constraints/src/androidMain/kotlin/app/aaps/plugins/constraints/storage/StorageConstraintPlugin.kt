package app.gmaaps.plugins.constraints.storage

import android.os.Environment
import android.os.StatFs
import app.gmaaps.annotations.OpenForTesting
import app.gmaaps.core.data.configuration.Constants
import app.gmaaps.core.data.plugin.PluginType
import app.gmaaps.core.interfaces.constraints.Constraint
import app.gmaaps.core.interfaces.constraints.PluginConstraints
import app.gmaaps.core.interfaces.di.APS
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.notifications.NotificationId
import app.gmaaps.core.interfaces.notifications.NotificationManager
import app.gmaaps.core.interfaces.plugin.EnforcedState
import app.gmaaps.core.interfaces.plugin.PluginBase
import app.gmaaps.core.interfaces.plugin.PluginDescription
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.keys.interfaces.TextRef.Companion.withArgs
import app.gmaaps.plugins.constraints.ConstraintsStrings
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.IntKey
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.binding

@OpenForTesting
@ContributesIntoMap(AppScope::class, binding = binding<PluginBase>())
@APS
@IntKey(820)
@SingleIn(AppScope::class)
@Inject
class StorageConstraintPlugin(
    aapsLogger: AAPSLogger,
    override val rh: TextResolver,
    notificationManager: NotificationManager
) : PluginBase(
    PluginDescription()
        .mainType(PluginType.CONSTRAINTS)
        .enforce(EnforcedState.Enabled)
        .showInList { false }
        .pluginName(ConstraintsStrings.storage),
    aapsLogger, rh, notificationManager
), PluginConstraints {

    override suspend fun isClosedLoopAllowed(value: Constraint<Boolean>): Constraint<Boolean> {
        val diskFree = availableInternalMemorySize()
        if (diskFree < Constants.MINIMUM_FREE_SPACE) {
            aapsLogger.debug(LTag.CONSTRAINTS, "Closed loop disabled. Internal storage free (Mb):$diskFree")
            value.set(false, rh.gs(ConstraintsStrings.disk_full, Constants.MINIMUM_FREE_SPACE), this)
            notificationManager.post(NotificationId.DISK_FULL, ConstraintsStrings.disk_full.withArgs(Constants.MINIMUM_FREE_SPACE))
        }
        return value
    }

    fun availableInternalMemorySize(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val blocksAvailable = stat.availableBlocksLong
        val size = 1048576 // block size of 1 Mb
        return blocksAvailable * blockSize / size
    }
}

