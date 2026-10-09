package app.gmaaps.pump.danars.comm

import app.gmaaps.core.interfaces.insulin.ConcentrationHelper
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.pump.BolusProgressData
import app.gmaaps.core.interfaces.pump.PumpInsulin
import app.gmaaps.pump.dana.DanaPump
import app.gmaaps.pump.danars.encryption.BleEncryption
import dev.zacsweers.metro.Inject

@Inject
class DanaRSPacketNotifyDeliveryRateDisplay(
    private val aapsLogger: AAPSLogger,
    private val ch: ConcentrationHelper,
    private val bolusProgressData: BolusProgressData,
    private val danaPump: DanaPump
) : DanaRSPacket() {

    init {
        type = BleEncryption.DANAR_PACKET__TYPE_NOTIFY
        opCode = BleEncryption.DANAR_PACKET__OPCODE_NOTIFY__DELIVERY_RATE_DISPLAY
    }

    override fun handleMessage(data: ByteArray) {
        val deliveredInsulin = byteArrayToInt(getBytes(data, DATA_START, 2)) / 100.0
        danaPump.bolusProgressLastTimeStamp = System.currentTimeMillis()
        failed = deliveredInsulin < danaPump.bolusingDetailedBolusInfo!!.insulin
        bolusProgressData.updateProgress(PumpInsulin(deliveredInsulin))
        aapsLogger.debug(LTag.PUMPCOMM, "Delivered insulin so far: $deliveredInsulin")
    }

    override val friendlyName: String = "NOTIFY__DELIVERY_RATE_DISPLAY"
}
