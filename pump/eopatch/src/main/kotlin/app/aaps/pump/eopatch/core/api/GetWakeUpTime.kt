package app.gmaaps.pump.eopatch.core.api

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.pump.eopatch.core.ble.BaseAPI
import app.gmaaps.pump.eopatch.core.ble.BytesConverter
import app.gmaaps.pump.eopatch.core.ble.PatchFunc
import app.gmaaps.pump.eopatch.core.response.WakeUpTimeResponse
import app.gmaaps.pump.eopatch.core.scan.IBleDevice
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.reactivex.rxjava3.core.Single

@SingleIn(AppScope::class)
@Inject
class GetWakeUpTime(patch: IBleDevice, aapsLogger: AAPSLogger) : BaseAPI<WakeUpTimeResponse>(PatchFunc.GET_WAKE_UP_TIME, patch, aapsLogger) {
    override fun parse(bytes: ByteArray): WakeUpTimeResponse {
        val success = bytes[DATA0].toInt() == 0
        if (!success) return WakeUpTimeResponse(false, 0)
        return WakeUpTimeResponse(true, BytesConverter.toUInt(bytes, DATA1))
    }

    fun get(): Single<WakeUpTimeResponse> = writeAndRead(generate())
}

