package app.gmaaps.pump.medtronic.comm.ui

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.pump.medtronic.comm.MedtronicCommunicationManager
import app.gmaaps.pump.medtronic.defs.MedtronicCommandType
import app.gmaaps.pump.medtronic.util.MedtronicUtil
import dev.zacsweers.metro.Inject

/**
 * Created by andy on 6/14/18.
 */
@Inject
class MedtronicUIComm(
    private val aapsLogger: AAPSLogger,
    private val medtronicUtil: MedtronicUtil,
    private val medtronicUIPostprocessor: MedtronicUIPostprocessor,
    private val medtronicCommunicationManager: MedtronicCommunicationManager,
    private val medtronicUITaskProvider: () -> MedtronicUITask
) {

    fun executeCommand(commandType: MedtronicCommandType): MedtronicUITask {
        return executeCommand(commandType, null)
    }

    @Synchronized
    fun executeCommand(commandType: MedtronicCommandType, parameters: ArrayList<Any>?): MedtronicUITask {

        aapsLogger.info(LTag.PUMP, "Execute Command: " + commandType.name)
        val task = medtronicUITaskProvider().with(commandType, parameters)
        medtronicUtil.setCurrentCommand(commandType)
        task.execute(medtronicCommunicationManager)

        if (!task.isReceived) {
            aapsLogger.warn(LTag.PUMP, "Reply not received for $commandType")
        }

        task.postProcess(medtronicUIPostprocessor)
        return task
    }

    val invalidResponsesCount: Int
        get() = medtronicCommunicationManager.getNotConnectedCount()

}
