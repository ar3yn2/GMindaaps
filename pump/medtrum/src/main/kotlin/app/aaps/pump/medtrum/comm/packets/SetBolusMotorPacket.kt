package app.gmaaps.pump.medtrum.comm.packets

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.pump.medtrum.comm.enums.CommandType.SET_BOLUS_MOTOR

class SetBolusMotorPacket(injector: MetroMemberInjector) : MedtrumPacket(injector) {

    // UNUSED in our driver

    init {
        opCode = SET_BOLUS_MOTOR.code
    }

    override fun getRequest(): ByteArray {
        return byteArrayOf(opCode) + 0.toByte()
    }
}

