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
import java.util.TimeZone
import kotlin.time.Duration.Companion.milliseconds

@SingleIn(AppScope::class)
@Inject
class SetGlobalTime(patch: IBleDevice, aapsLogger: AAPSLogger) : BaseBooleanAPI(PatchFunc.SET_GLOBAL_TIME, patch, aapsLogger) {

    fun set(): Single<PatchBooleanResponse> = writeAndRead(generate())

    override fun generate(): ByteArray {
        val now = System.currentTimeMillis()
        val diffSecs = (now / 1000).toInt()
        val offset = TimeZone.getDefault().getOffset(now)
        val timeZoneOffset = offset.toLong().milliseconds.inWholeMinutes.toInt() / 15
        return allocate().putInt(diffSecs).putByte(0).putByte(timeZoneOffset).build()
    }
}

