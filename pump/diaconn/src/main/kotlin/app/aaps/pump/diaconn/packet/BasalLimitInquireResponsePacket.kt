package app.gmaaps.pump.diaconn.packet

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.resources.ResourceHelper
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.pump.diaconn.DiaconnG8Pump
import app.gmaaps.pump.diaconn.keys.DiaconnStringNonKey
import app.gmaaps.pump.diaconn.pumplog.PumpLogUtil
import dev.zacsweers.metro.Inject

/**
 * BasalLimitInquireResponsePacket
 */
class BasalLimitInquireResponsePacket(injector: MetroMemberInjector) : DiaconnG8Packet(injector) {

    @Inject lateinit var diaconnG8Pump: DiaconnG8Pump
    @Inject lateinit var preferences: Preferences
    @Inject lateinit var rh: ResourceHelper

    init {
        msgType = 0x92.toByte()
        aapsLogger.debug(LTag.PUMPCOMM, "BasalLimitInquireResponsePacket init")
    }

    override fun handleMessage(data: ByteArray) {
        val result = defect(data)
        if (result != 0) {
            aapsLogger.debug(LTag.PUMPCOMM, "BasalLimitInquireResponsePacket Got some Error")
            failed = true
            return
        } else failed = false

        val bufferData = prefixDecode(data)
        val result2 = getByteToInt(bufferData)
        if (!isSuccInquireResponseResult(result2)) {
            failed = true
            return
        }
        diaconnG8Pump.maxBasalPerHours = getShortToInt(bufferData).toDouble() / 100.0  // not include tempbasal limit
        val pumpFirmwareVersion = preferences.get(DiaconnStringNonKey.PumpVersion)
        if (pumpFirmwareVersion.isNotEmpty() && PumpLogUtil.isPumpVersionGe(pumpFirmwareVersion, 3, 0)) {
            diaconnG8Pump.maxBasal = diaconnG8Pump.maxBasalPerHours * 2.5 // include tempbasal
        } else {
            diaconnG8Pump.maxBasal = diaconnG8Pump.maxBasalPerHours * 2.0 // include tempbasal
        }

        aapsLogger.debug(LTag.PUMPCOMM, "Result --> ${diaconnG8Pump.result}")
        aapsLogger.debug(LTag.PUMPCOMM, "maxBasal --> ${diaconnG8Pump.maxBasal}")
    }

    override val friendlyName = "PUMP_BASAL_LIMIT_INQUIRE_RESPONSE"
}
