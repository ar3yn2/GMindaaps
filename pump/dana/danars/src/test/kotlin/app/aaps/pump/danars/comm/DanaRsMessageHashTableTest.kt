package app.gmaaps.pump.danars.comm

import app.gmaaps.core.interfaces.pump.PumpSync
import app.gmaaps.pump.danars.DanaRSTestBase
import app.gmaaps.pump.danars.services.DanaRSAlarmReporter
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock

class DanaRsMessageHashTableTest : DanaRSTestBase() {

    @Mock lateinit var pumpSync: PumpSync

    lateinit var packetList: Set<DanaRSPacket>

    @BeforeEach
    fun setupMock() {
        packetList = setOf(
            DanaRSPacketNotifyAlarm(aapsLogger, rh, DanaRSAlarmReporter(rh, notificationManager, pumpSync, danaPump)),
            DanaRSPacketNotifyDeliveryComplete(aapsLogger, ch, bolusProgressData, danaPump),
            DanaRSPacketNotifyDeliveryRateDisplay(aapsLogger, ch, bolusProgressData, danaPump),
            DanaRSPacketNotifyMissedBolusAlarm(aapsLogger)
        )
    }

    @Test
    fun findMessage() {
        val danaRSMessageHashTable = DanaRSMessageHashTable(packetList)
        val command = DanaRSPacketNotifyMissedBolusAlarm(aapsLogger).command
        Assertions.assertTrue(danaRSMessageHashTable.findMessage(command) is DanaRSPacketNotifyMissedBolusAlarm)
    }

    @Test
    fun returnsNullForUnknownMessage() {
        // Was assertThrows: findMessage now returns null instead of throwing, so an unexpected packet
        // arriving on the BLE callback thread is logged as "Unknown message" by BLEComm.processMessage
        // rather than crashing the app. See DanaRSMessageHashTable.findMessage.
        val danaRSMessageHashTable = DanaRSMessageHashTable(packetList)
        val command = DanaRSPacket().command
        assertThat(danaRSMessageHashTable.findMessage(command)).isNull()
    }
}
