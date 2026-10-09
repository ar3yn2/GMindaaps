package app.gmaaps.pump.danar.comm

import app.gmaaps.pump.dana.DanaPump
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MsgSettingShippingInfoTest : DanaRTestBase() {

    @Test fun runTest() {
        val packet = MsgSettingShippingInfo(injector)
        danaPump.units = DanaPump.UNITS_MGDL
        // test message decoding
        packet.handleMessage(createArray(34, 7.toByte()))
        Assertions.assertEquals(MessageBase.stringFromBuff(createArray(34, 7.toByte()), 0, 10), danaPump.serialNumber)
    }
}
