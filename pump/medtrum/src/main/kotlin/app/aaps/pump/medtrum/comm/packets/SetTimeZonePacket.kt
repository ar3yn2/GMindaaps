package app.gmaaps.pump.medtrum.comm.packets

import app.gmaaps.core.data.time.T
import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.pump.medtrum.MedtrumPump
import app.gmaaps.pump.medtrum.comm.enums.CommandType.SET_TIME_ZONE
import app.gmaaps.pump.medtrum.extension.toByteArray
import app.gmaaps.pump.medtrum.util.MedtrumTimeUtil
import dev.zacsweers.metro.Inject

class SetTimeZonePacket(injector: MetroMemberInjector) : MedtrumPacket(injector) {

    @Inject lateinit var dateUtil: DateUtil
    @Inject lateinit var medtrumPump: MedtrumPump
    @Inject lateinit var medtrumTimeUtil: MedtrumTimeUtil

    private val offsetMinutes = dateUtil.getTimeZoneOffsetMinutes(dateUtil.now())

    init {
        opCode = SET_TIME_ZONE.code
    }

    override fun getRequest(): ByteArray {
        val time = medtrumTimeUtil.getCurrentTimePumpSeconds()
        var calcOffset = offsetMinutes
        aapsLogger.debug(LTag.PUMPCOMM, "Requested offset: $calcOffset minutes")
        // Workaround for bug where it fails to set timezone > GMT + 12
        // if offset is > 12 hours, subtract 24 hours
        if (calcOffset > T.hours(12).mins()) {
            calcOffset -= T.hours(24).mins().toInt()
            aapsLogger.debug(LTag.PUMPCOMM, "Modifying requested offset to: $calcOffset minutes")
        }
        // Pump expects this for negative offsets
        if (calcOffset < 0) calcOffset += 65536
        return byteArrayOf(opCode) + calcOffset.toByteArray(2) + time.toByteArray(4)
    }

    override fun handleResponse(data: ByteArray): Boolean {
        val success = super.handleResponse(data)
        if (success) {
            medtrumPump.pumpTimeZoneOffset = offsetMinutes
        }
        return success
    }
}

