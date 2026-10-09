package app.gmaaps.pump.medtrum.comm.packets

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.pump.medtrum.comm.enums.CommandType.SUBSCRIBE
import app.gmaaps.pump.medtrum.extension.toByteArray

class SubscribePacket(injector: MetroMemberInjector) : MedtrumPacket(injector) {

    init {
        opCode = SUBSCRIBE.code
    }

    override fun getRequest(): ByteArray {
        return byteArrayOf(opCode) + 4095.toByteArray(2)
    }
}

