package app.gmaaps.plugins.sync.xdrip.workers

import android.content.Context
import androidx.work.WorkerParameters
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.sync.DataSyncSelectorXdrip
import app.gmaaps.core.interfaces.utils.fabric.FabricPrivacy
import app.gmaaps.core.objects.workflow.LoggingWorker
import app.gmaaps.core.objects.workflow.WorkerInstanceFactory
import app.gmaaps.plugins.sync.xdrip.compose.XdripMvvmRepository
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.Dispatchers

@AssistedInject
class XdripDataSyncWorker(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    aapsLogger: AAPSLogger,
    fabricPrivacy: FabricPrivacy,
    private val dataSyncSelector: DataSyncSelectorXdrip,
    private val activePlugin: ActivePlugin,
    private val xdripMvvmRepository: XdripMvvmRepository
) : LoggingWorker(context, params, Dispatchers.IO, aapsLogger, fabricPrivacy) {

    override suspend fun doWorkAndLog(): Result {
        xdripMvvmRepository.addLog("UPL", "Start")
        dataSyncSelector.doUpload()
        xdripMvvmRepository.addLog("UPL", "End")
        xdripMvvmRepository.updateQueueSize(dataSyncSelector.queueSize())
        return Result.success()
    }

    /** Metro builds the worker through this - WorkManager supplies context and params. */
    @AssistedFactory
    abstract class Factory : WorkerInstanceFactory<XdripDataSyncWorker>()
}

