package app.gmaaps.pump.eopatch.core.api

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.pump.eopatch.core.ble.BaseAPI
import app.gmaaps.pump.eopatch.core.ble.BytesConverter
import app.gmaaps.pump.eopatch.core.ble.PatchFunc
import app.gmaaps.pump.eopatch.core.response.PatchInternalSuspendTimeResponse
import app.gmaaps.pump.eopatch.core.scan.IBleDevice
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.reactivex.rxjava3.core.Single

@SingleIn(AppScope::class)
@Inject
class GetInternalSuspendTime(patch: IBleDevice, aapsLogger: AAPSLogger) : BaseAPI<PatchInternalSuspendTimeResponse>(PatchFunc.GET_INTERNAL_SUSPENDED_TIME, patch, aapsLogger) {
    override fun parse(bytes: ByteArray): PatchInternalSuspendTimeResponse {
        val success = bytes[DATA0].toInt() == 0
        return if (success) PatchInternalSuspendTimeResponse(true, BytesConverter.toInt(bytes, DATA1))
        else PatchInternalSuspendTimeResponse(false, 0)
    }

    fun get(): Single<PatchInternalSuspendTimeResponse> = writeAndRead(generate())
}

