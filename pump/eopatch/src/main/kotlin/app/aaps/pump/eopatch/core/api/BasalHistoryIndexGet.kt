package app.gmaaps.pump.eopatch.core.api

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.pump.eopatch.core.ble.BaseAPI
import app.gmaaps.pump.eopatch.core.ble.BytesConverter
import app.gmaaps.pump.eopatch.core.ble.PatchFunc
import app.gmaaps.pump.eopatch.core.response.BasalHistoryIndexResponse
import app.gmaaps.pump.eopatch.core.scan.IBleDevice
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.reactivex.rxjava3.core.Single

@SingleIn(AppScope::class)
@Inject
class BasalHistoryIndexGet(patch: IBleDevice, aapsLogger: AAPSLogger) : BaseAPI<BasalHistoryIndexResponse>(PatchFunc.GET_BASAL_HISTORY_INDEX, patch, aapsLogger) {
    override fun parse(bytes: ByteArray): BasalHistoryIndexResponse {
        val lastIndex = BytesConverter.toUInt(bytes[DATA1], bytes[DATA2])
        val curIndex = BytesConverter.toUInt(bytes[DATA3], bytes[DATA4])
        return BasalHistoryIndexResponse(lastIndex, curIndex)
    }

    fun get(): Single<BasalHistoryIndexResponse> = writeAndRead(generate())
}

