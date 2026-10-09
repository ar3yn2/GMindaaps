package app.gmaaps.pump.medtrum.comm.packets

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.pump.medtrum.MedtrumTestBase
import app.gmaaps.pump.medtrum.extension.toByteArray
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SubscribePacketTest : MedtrumTestBase() {

    /** Test packet specific behavior */

    private val packetInjector = MetroMemberInjector {
        if (it is MedtrumPacket) {
                it.aapsLogger = aapsLogger
        }
        true
    }

    @Test fun getRequestGivenPacketWhenCalledThenReturnOpCode() {
        // Inputs
        val opCode = 4

        // Call
        val packet = SubscribePacket(packetInjector)
        val result = packet.getRequest()

        // Expected values
        val expected = byteArrayOf(opCode.toByte()) + 4095.toByteArray(2)
        assertThat(result).asList().containsExactlyElementsIn(expected.toList()).inOrder()
    }
}

