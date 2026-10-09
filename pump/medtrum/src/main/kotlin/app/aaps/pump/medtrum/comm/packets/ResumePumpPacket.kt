package app.gmaaps.pump.medtrum.comm.packets

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.pump.medtrum.comm.enums.CommandType.RESUME_PUMP

class ResumePumpPacket(injector: MetroMemberInjector) : MedtrumPacket(injector) {

    init {
        opCode = RESUME_PUMP.code
    }

    override fun getRequest(): ByteArray {
        return byteArrayOf(opCode)
    }
}

