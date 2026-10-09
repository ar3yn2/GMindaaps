package app.gmaaps.plugins.aps.loop.runningMode

import android.content.Context
import androidx.work.WorkerParameters
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.utils.fabric.FabricPrivacy
import app.gmaaps.core.objects.workflow.LoggingWorker
import app.gmaaps.core.objects.workflow.MetroWorkerCreator
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.Dispatchers

/**
 * WorkManager trigger for [RunningModeExpiryJob]. The job holds what happens and why; this holds only
 * when.
 * Note that Android types were never the problem: [LoggingWorker], which this extends, has always
 * lived in a multiplatform module. `androidMain` compiles against the Android SDK like any other
 * Android source set. The line was the annotation processor, not WorkManager.
 */
@AssistedInject
class RunningModeExpiryWorker(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    aapsLogger: AAPSLogger,
    fabricPrivacy: FabricPrivacy,
    private val job: RunningModeExpiryJob
) : LoggingWorker(context, params, Dispatchers.Default, aapsLogger, fabricPrivacy) {

    override suspend fun doWorkAndLog(): Result {
        job.run()
        return Result.success()
    }

    @AssistedFactory
    fun interface Factory : MetroWorkerCreator {

        override fun create(context: Context, params: WorkerParameters): RunningModeExpiryWorker
    }

    companion object {

        const val WORK_NAME = "RunningModeExpiry"
    }
}

