package app.gmaaps.pump.eopatch.core.api

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.pump.eopatch.core.ble.BaseAPI
import app.gmaaps.pump.eopatch.core.ble.BytesConverter
import app.gmaaps.pump.eopatch.core.ble.PatchFunc
import app.gmaaps.pump.eopatch.core.code.PatchBleResultCode
import app.gmaaps.pump.eopatch.core.response.TempBasalFinishTimeResponse
import app.gmaaps.pump.eopatch.core.scan.IBleDevice
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.reactivex.rxjava3.core.Single

@SingleIn(AppScope::class)
@Inject
class TempBasalFinishTimeGet(patch: IBleDevice, aapsLogger: AAPSLogger) : BaseAPI<TempBasalFinishTimeResponse>(PatchFunc.GET_TEMP_BASAL_FINISH_TIME, patch, aapsLogger) {
    override fun parse(bytes: ByteArray): TempBasalFinishTimeResponse {
        val ret = bytes[DATA0].toInt()
        var tempBasalFinishTime = 0
        val result = when (ret) {
            0    -> { tempBasalFinishTime = BytesConverter.toUInt(bytes, DATA1); PatchBleResultCode.SUCCESS }
            0x01 -> PatchBleResultCode.TEMP_BASAL_NOT_EXIST
            0x02 -> PatchBleResultCode.TEMP_BASAL_NOT_FINISH
            else -> PatchBleResultCode.UNKNOWN_ERROR
        }
        return TempBasalFinishTimeResponse(tempBasalFinishTime, result)
    }

    fun get(): Single<TempBasalFinishTimeResponse> = writeAndRead(generate())
}

