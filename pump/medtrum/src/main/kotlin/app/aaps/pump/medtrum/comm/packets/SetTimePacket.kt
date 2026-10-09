package app.gmaaps.pump.medtrum.comm.packets

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.pump.medtrum.comm.enums.CommandType.SET_TIME
import app.gmaaps.pump.medtrum.extension.toByteArray
import app.gmaaps.pump.medtrum.util.MedtrumTimeUtil
import dev.zacsweers.metro.Inject

class SetTimePacket(injector: MetroMemberInjector) : MedtrumPacket(injector) {

    @Inject lateinit var medtrumTimeUtil: MedtrumTimeUtil

    init {
        opCode = SET_TIME.code
    }

    override fun getRequest(): ByteArray {
        val time = medtrumTimeUtil.getCurrentTimePumpSeconds()
        return byteArrayOf(opCode) + 2.toByte() + time.toByteArray(4)
    }
}

