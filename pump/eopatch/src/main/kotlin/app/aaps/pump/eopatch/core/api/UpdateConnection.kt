package app.gmaaps.pump.eopatch.core.api

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.pump.eopatch.core.ble.BaseAPI
import app.gmaaps.pump.eopatch.core.ble.PatchFunc
import app.gmaaps.pump.eopatch.core.response.UpdateConnectionResponse
import app.gmaaps.pump.eopatch.core.scan.IBleDevice
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.reactivex.rxjava3.core.Single

@SingleIn(AppScope::class)
@Inject
class UpdateConnection(patch: IBleDevice, aapsLogger: AAPSLogger) : BaseAPI<UpdateConnectionResponse>(PatchFunc.UPDATE_CONNECTION, patch, aapsLogger) {
    override fun parse(bytes: ByteArray): UpdateConnectionResponse {
        val newState = ByteArray(SIZE)
        System.arraycopy(bytes, NOOP1, newState, 0, SIZE)
        return UpdateConnectionResponse(newState)
    }

    fun get(): Single<UpdateConnectionResponse> = writeAndRead(generate())
    override fun generate(): ByteArray = allocate().putByte(1).putByte(0).build()

    companion object {
        private const val SIZE = 20
    }
}

