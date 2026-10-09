package app.gmaaps.pump.medtrum.comm.packets

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.pump.medtrum.comm.enums.CommandType.POLL_PATCH

class PollPatchPacket(injector: MetroMemberInjector) : MedtrumPacket(injector) {

    init {
        opCode = POLL_PATCH.code
    }
}

