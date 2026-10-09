package app.gmaaps.pump.danars.comm

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.pump.danars.encryption.BleEncryption
import dev.zacsweers.metro.Inject

@Inject
class DanaRSPacketEtcKeepConnection(
    private val aapsLogger: AAPSLogger
) : DanaRSPacket() {

    init {
        opCode = BleEncryption.DANAR_PACKET__OPCODE_ETC__KEEP_CONNECTION
        aapsLogger.debug(LTag.PUMPCOMM, "New message")
    }

    override fun handleMessage(data: ByteArray) {
        val result = intFromBuff(data, 0, 1)
        @Suppress("LiftReturnOrAssignment")
        if (result == 0) {
            aapsLogger.debug(LTag.PUMPCOMM, "Result OK")
            failed = false
        } else {
            aapsLogger.error("Result Error: $result")
            failed = true
        }
    }

    override val friendlyName: String = "ETC__KEEP_CONNECTION"
}
