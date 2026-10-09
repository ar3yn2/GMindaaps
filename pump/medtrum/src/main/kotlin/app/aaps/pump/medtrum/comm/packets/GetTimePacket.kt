package app.gmaaps.pump.medtrum.comm.packets

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.pump.medtrum.MedtrumPump
import app.gmaaps.pump.medtrum.comm.enums.CommandType.GET_TIME
import app.gmaaps.pump.medtrum.extension.toLong
import app.gmaaps.pump.medtrum.util.MedtrumTimeUtil
import dev.zacsweers.metro.Inject

class GetTimePacket(injector: MetroMemberInjector) : MedtrumPacket(injector) {

    @Inject lateinit var medtrumPump: MedtrumPump
    @Inject lateinit var medtrumTimeUtil: MedtrumTimeUtil

    companion object {

        private const val RESP_TIME_START = 6
        private const val RESP_TIME_END = RESP_TIME_START + 4
    }

    init {
        opCode = GET_TIME.code
        expectedMinRespLength = RESP_TIME_END
    }

    override fun handleResponse(data: ByteArray): Boolean {
        val success = super.handleResponse(data)
        if (success) {
            val time = medtrumTimeUtil.convertPumpTimeToSystemTimeMillis(data.copyOfRange(RESP_TIME_START, RESP_TIME_END).toLong())
            medtrumPump.lastTimeReceivedFromPump = time
        }

        return success
    }
}

