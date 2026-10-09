package app.gmaaps.pump.danarv2.comm

import app.gmaaps.pump.dana.DanaPump
import app.gmaaps.pump.danar.comm.DanaRTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MsgCheckValueRv2Test : DanaRTestBase() {

    @Test
    fun runTest() {
        val packet = MsgCheckValueV2(injector)
        // test message decoding
        val array = createArray(34, 3.toByte())
        array[7] = 2.toByte() // protocol (at buffOffset=1 + 6) must be 2 to avoid reset branch
        packet.handleMessage(array)
        Assertions.assertEquals(DanaPump.EXPORT_MODEL, danaPump.hwModel)
    }
}
