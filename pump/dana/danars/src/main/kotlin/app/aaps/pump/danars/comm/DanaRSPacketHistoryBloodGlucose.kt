package app.gmaaps.pump.danars.comm

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.pump.PumpSync
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.pump.dana.DanaPump
import app.gmaaps.pump.dana.database.DanaHistoryRecordDao
import app.gmaaps.pump.danars.encryption.BleEncryption
import dev.zacsweers.metro.Inject

@Inject
class DanaRSPacketHistoryBloodGlucose(
    aapsLogger: AAPSLogger,
    dateUtil: DateUtil,
    rxBus: RxBus,
    danaHistoryRecordDao: DanaHistoryRecordDao,
    pumpSync: PumpSync,
    danaPump: DanaPump
) : DanaRSPacketHistory(aapsLogger, dateUtil, rxBus, danaHistoryRecordDao, pumpSync, danaPump) {

    init {
        opCode = BleEncryption.DANAR_PACKET__OPCODE_REVIEW__BLOOD_GLUCOSE
        aapsLogger.debug(LTag.PUMPCOMM, "New message")
    }

    override val friendlyName: String = "REVIEW__BLOOD_GLUCOSE"
}
