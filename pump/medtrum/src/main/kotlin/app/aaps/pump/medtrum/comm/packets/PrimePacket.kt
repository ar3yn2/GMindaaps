package app.gmaaps.pump.medtrum.comm.packets

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.pump.medtrum.comm.enums.CommandType.PRIME

class PrimePacket(injector: MetroMemberInjector) : MedtrumPacket(injector) {

    init {
        opCode = PRIME.code
    }
}

