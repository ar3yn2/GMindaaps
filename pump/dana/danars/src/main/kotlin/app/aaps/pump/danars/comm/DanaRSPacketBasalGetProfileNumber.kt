package app.gmaaps.pump.danars.comm

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.pump.dana.DanaPump
import app.gmaaps.pump.danars.encryption.BleEncryption
import dev.zacsweers.metro.Inject

@Inject
class DanaRSPacketBasalGetProfileNumber(
    private val aapsLogger: AAPSLogger,
    private val danaPump: DanaPump
) : DanaRSPacket() {

    init {
        opCode = BleEncryption.DANAR_PACKET__OPCODE_BASAL__GET_PROFILE_NUMBER
        aapsLogger.debug(LTag.PUMPCOMM, "Requesting active profile")
    }

    override fun handleMessage(data: ByteArray) {
        danaPump.activeProfile = byteArrayToInt(getBytes(data, DATA_START, 1))
        aapsLogger.debug(LTag.PUMPCOMM, "Active profile: " + danaPump.activeProfile)
    }

    override val friendlyName: String = "BASAL__GET_PROFILE_NUMBER"
}
