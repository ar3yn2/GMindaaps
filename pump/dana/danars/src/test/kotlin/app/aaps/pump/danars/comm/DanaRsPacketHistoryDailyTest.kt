package app.gmaaps.pump.danars.comm

import app.gmaaps.core.interfaces.pump.PumpSync
import app.gmaaps.pump.dana.database.DanaHistoryRecordDao
import app.gmaaps.pump.danars.DanaRSTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mock

class DanaRsPacketHistoryDailyTest : DanaRSTestBase() {

    @Mock lateinit var danaHistoryRecordDao: DanaHistoryRecordDao
    @Mock lateinit var pumpSync: PumpSync

    @Test fun runTest() {
        val packet = DanaRSPacketHistoryDaily(aapsLogger, dateUtil, rxBus, danaHistoryRecordDao, pumpSync, danaPump).with(System.currentTimeMillis())
        Assertions.assertEquals("REVIEW__DAILY", packet.friendlyName)
    }
}
