package app.gmaaps.pump.eopatch.core.api

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.pump.eopatch.core.ble.BaseAPI
import app.gmaaps.pump.eopatch.core.ble.BytesConverter
import app.gmaaps.pump.eopatch.core.ble.PatchFunc
import app.gmaaps.pump.eopatch.core.response.BatteryVoltageLevelPairingResponse
import app.gmaaps.pump.eopatch.core.scan.IBleDevice
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.reactivex.rxjava3.core.Single

@SingleIn(AppScope::class)
@Inject
class GetVoltageLevelB4Priming(patch: IBleDevice, aapsLogger: AAPSLogger) : BaseAPI<BatteryVoltageLevelPairingResponse>(PatchFunc.GET_VOLTAGE_B4_PRIMING, patch, aapsLogger) {
    override fun parse(bytes: ByteArray) = BatteryVoltageLevelPairingResponse(
        BytesConverter.toUInt(bytes[DATA0], bytes[DATA1]),
        BytesConverter.toUInt(bytes[DATA2]),
        bytes[DATA4].toInt()
    )

    fun get(): Single<BatteryVoltageLevelPairingResponse> = writeAndRead(generate())
}

