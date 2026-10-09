package app.gmaaps.pump.diaconn.packet

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.pump.diaconn.DiaconnG8Pump
import dev.zacsweers.metro.Inject

/**
 * RejectReportPacket
 */
class RejectReportPacket(
    injector: MetroMemberInjector
) : DiaconnG8Packet(injector) {

    @Inject lateinit var diaconnG8Pump: DiaconnG8Pump
    var reqMsgType: Int? = null
    var reason: Int? = null

    init {
        msgType = 0xE2.toByte()
        aapsLogger.debug(LTag.PUMPCOMM, "RejectReportPacket init ")
    }

    override fun handleMessage(data: ByteArray) {
        val defectCheck = defect(data)
        if (defectCheck != 0) {
            aapsLogger.debug(LTag.PUMPCOMM, "RejectReportPacket Got some Error")
            failed = true
            return
        } else failed = false

        val bufferData = prefixDecode(data)
        reqMsgType = getByteToInt(bufferData)
        reason = getByteToInt(bufferData)
        aapsLogger.debug(LTag.PUMPCOMM, "reqMsgType --> $reqMsgType")
        aapsLogger.debug(LTag.PUMPCOMM, "Reject Reason --> $reason (6:cancel, 10:timeout) ")
    }

    override val friendlyName = "PUMP_REJECT_REPORT"
}
