package app.gmaaps.pump.medtrum.comm.packets

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.pump.medtrum.comm.enums.CommandType.SET_BOLUS
import app.gmaaps.pump.medtrum.extension.toByteArray
import kotlin.math.round

class SetBolusPacket(injector: MetroMemberInjector, private val insulin: Double) : MedtrumPacket(injector) {

    init {
        opCode = SET_BOLUS.code
    }

    override fun getRequest(): ByteArray {
        // Bolus types:
        // 1 = normal
        // 2 = Extended
        // 3 = Combi
        val bolusType: Byte = 1 // Only support for normal bolus for now
        val bolusAmount: Int = round(insulin / 0.05).toInt()
        return byteArrayOf(opCode) + bolusType + bolusAmount.toByteArray(2) + 0.toByte()
    }
}

