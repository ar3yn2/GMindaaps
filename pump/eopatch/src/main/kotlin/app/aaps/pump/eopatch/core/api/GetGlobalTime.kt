package app.gmaaps.pump.eopatch.core.api

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.pump.eopatch.core.ble.BaseAPI
import app.gmaaps.pump.eopatch.core.ble.BytesConverter
import app.gmaaps.pump.eopatch.core.ble.PatchFunc
import app.gmaaps.pump.eopatch.core.response.GlobalTimeResponse
import app.gmaaps.pump.eopatch.core.scan.IBleDevice
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.reactivex.rxjava3.core.Single

@SingleIn(AppScope::class)
@Inject
class GetGlobalTime(patch: IBleDevice, aapsLogger: AAPSLogger) : BaseAPI<GlobalTimeResponse>(PatchFunc.GET_GLOBAL_TIME, patch, aapsLogger) {
    override fun parse(bytes: ByteArray): GlobalTimeResponse {
        val time = BytesConverter.toUInt(bytes, DATA0)
        return GlobalTimeResponse(time, bytes[DATA5].toInt())
    }

    fun get(@Suppress("UNUSED_PARAMETER") clearHistory: Boolean): Single<GlobalTimeResponse> = writeAndRead(generate())
    override fun generate(): ByteArray = allocate().putByte(0).build()
}

