package app.gmaaps.pump.danar.comm

import app.gmaaps.core.interfaces.pump.DetailedBolusInfo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MsgBolusStopTest : DanaRTestBase() {

    @Test fun runTest() {
//        whenever(rh.gs(app.gmaaps.pump.dana.R.string.overview_bolusprogress_delivered)).thenReturn("Delivered")
        danaPump.bolusingDetailedBolusInfo = DetailedBolusInfo()
        val packet = MsgBolusStop(injector)

        // test message decoding
        packet.handleMessage(ByteArray(100))
        Assertions.assertEquals(true, danaPump.bolusStopped)
    }
}
