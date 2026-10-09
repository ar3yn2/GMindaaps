package app.gmaaps.plugins.sync.openhumans

import android.content.Context
import androidx.work.WorkerParameters
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.utils.fabric.FabricPrivacy
import app.gmaaps.core.objects.workflow.LoggingWorker
import app.gmaaps.core.objects.workflow.MetroWorkerCreator
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.Dispatchers

@AssistedInject
class OpenHumansWorker(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    aapsLogger: AAPSLogger,
    fabricPrivacy: FabricPrivacy,
    private val openHumansUploader: OpenHumansUploaderPlugin
) : LoggingWorker(context, params, Dispatchers.IO, aapsLogger, fabricPrivacy) {

    override suspend fun doWorkAndLog(): Result {
        return try {
            aapsLogger.info(LTag.OHUPLOADER, "Starting upload")
            openHumansUploader.uploadData()
            aapsLogger.info(LTag.OHUPLOADER, "Upload finished")
            Result.success()
        } catch (e: Exception) {
            aapsLogger.error(LTag.OHUPLOADER, "OH Uploader failed", e)
            Result.failure()
        }
    }

    /** Metro builds the worker through this. */
    @AssistedFactory
    fun interface Factory : MetroWorkerCreator {

        override fun create(context: Context, params: WorkerParameters): OpenHumansWorker
    }
}

