package app.gmaaps.pump.eopatch.core.api

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.pump.eopatch.core.ble.BaseAPI
import app.gmaaps.pump.eopatch.core.ble.BytesConverter
import app.gmaaps.pump.eopatch.core.ble.PatchFunc
import app.gmaaps.pump.eopatch.core.response.BolusFinishTimeResponse
import app.gmaaps.pump.eopatch.core.scan.IBleDevice
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.reactivex.rxjava3.core.Single

@SingleIn(AppScope::class)
@Inject
class BolusFinishTimeGet(patch: IBleDevice, aapsLogger: AAPSLogger) : BaseAPI<BolusFinishTimeResponse>(PatchFunc.GET_BOLUS_FINISH_TIME, patch, aapsLogger) {
    override fun parse(bytes: ByteArray): BolusFinishTimeResponse {
        val now = BytesConverter.toUInt(bytes, DATA0)
        val ext = BytesConverter.toUInt(bytes, DATA4)
        return BolusFinishTimeResponse(now, ext)
    }

    fun get(): Single<BolusFinishTimeResponse> = writeAndRead(generate())
}

