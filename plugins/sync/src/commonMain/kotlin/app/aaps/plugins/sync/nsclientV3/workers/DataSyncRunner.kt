package app.gmaaps.plugins.sync.nsclientV3.workers

import app.gmaaps.core.data.time.T
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.nsclient.NSClientRepository
import app.gmaaps.core.interfaces.plugin.ActivePlugin

import app.gmaaps.core.objects.workflow.WorkOutcome

import app.gmaaps.plugins.sync.nsclientV3.DataSyncSelectorV3
import app.gmaaps.plugins.sync.nsclientV3.NSClientV3Plugin
import dev.zacsweers.metro.Inject

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

@Inject
class DataSyncRunner(

    private val aapsLogger: AAPSLogger,

    private val dataSyncSelectorV3: DataSyncSelectorV3,
    private val activePlugin: ActivePlugin,
    private val nsClientV3Plugin: NSClientV3Plugin,
    private val nsClientRepository: NSClientRepository
) {

    suspend fun run(): WorkOutcome {
        if (nsClientV3Plugin.doingFullSync) {
            nsClientRepository.addLog("● RUN", "Full sync finished")
            nsClientV3Plugin.endFullSync()
        }
        if (nsClientV3Plugin.hasWritePermission || nsClientV3Plugin.wsConnectedFlow.value) {
            nsClientRepository.addLog("► UPL", "Start")
            try {
                // Hard cap so a hung HTTP call / dead WS can't keep the worker in
                // RUNNING/BLOCKED forever and silently block every future upload.
                withTimeout(UPLOAD_TIMEOUT_MS) { dataSyncSelectorV3.doUpload() }
                nsClientRepository.addLog("► UPL", "End")
            } catch (e: TimeoutCancellationException) {
                nsClientRepository.addLog("◄ ERROR", "Upload timed out")
                aapsLogger.error(LTag.NSCLIENT, "DataSyncWorker timed out", e)
                return WorkOutcome.Failure("Upload timed out")
            }
        } else {
            // Both conditions are negated. This is the else of `hasWritePermission || connected`, so
            // writing them the same way round as the `if` above would make both unreachable and
            // neither message would ever be logged.
            if (!nsClientV3Plugin.hasWritePermission)
                nsClientRepository.addLog("► ERROR", "No write permission")
            else
                nsClientRepository.addLog("► ERROR", "Not connected")
            // refresh token
            nsClientV3Plugin.scheduleIrregularExecution(refreshToken = true)
        }
        return WorkOutcome.Success
    }

    companion object {

        private val UPLOAD_TIMEOUT_MS = T.mins(30).msecs()
    }
}

