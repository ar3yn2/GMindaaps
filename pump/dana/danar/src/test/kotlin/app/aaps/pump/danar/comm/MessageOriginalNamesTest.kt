package app.gmaaps.pump.danar.comm

import app.gmaaps.shared.tests.TestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MessageOriginalNamesTest : TestBase() {

    @Test
    fun getNameTest() {
        Assertions.assertEquals("CMD_CONNECT", MessageOriginalNames.getName(0x3001))
    }
}
