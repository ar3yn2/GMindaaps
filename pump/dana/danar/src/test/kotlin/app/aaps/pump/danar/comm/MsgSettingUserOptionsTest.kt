package app.gmaaps.pump.danar.comm

import app.gmaaps.pump.dana.DanaPump
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MsgSettingUserOptionsTest : DanaRTestBase() {

    @Test fun runTest() {
        val packet = MsgSettingUserOptions(injector)
        danaPump.units = DanaPump.UNITS_MGDL
        // test message decoding
        packet.handleMessage(createArray(48, 7.toByte()))
        Assertions.assertEquals(7, danaPump.lcdOnTimeSec)
    }
}
