package app.gmaaps.pump.danar.comm

import app.gmaaps.pump.dana.comm.RecordTypes
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RecordTypesTest {

    @Test fun runTest() {
        Assertions.assertEquals(1.toByte(), RecordTypes.RECORD_TYPE_BOLUS)
    }
}
