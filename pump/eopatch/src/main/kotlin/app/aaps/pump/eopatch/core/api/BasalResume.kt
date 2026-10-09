package app.gmaaps.pump.eopatch.core.api

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.pump.eopatch.core.ble.BaseBooleanAPI
import app.gmaaps.pump.eopatch.core.ble.PatchFunc
import app.gmaaps.pump.eopatch.core.response.PatchBooleanResponse
import app.gmaaps.pump.eopatch.core.scan.IBleDevice
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.reactivex.rxjava3.core.Single

@SingleIn(AppScope::class)
@Inject
class BasalResume(patch: IBleDevice, aapsLogger: AAPSLogger) : BaseBooleanAPI(PatchFunc.RESUME_NORMAL_BASAL, patch, aapsLogger) {
    override fun generate(): ByteArray = allocate().putByte(DEFAULT).build()
    fun resume(): Single<PatchBooleanResponse> = writeAndRead(generate())

    companion object {
        const val DEFAULT = 0
    }
}

