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
import kotlin.math.roundToInt

@SingleIn(AppScope::class)
@Inject
class BasalPause(patch: IBleDevice, aapsLogger: AAPSLogger) : BaseBooleanAPI(PatchFunc.PAUSE_BASAL, patch, aapsLogger) {
    fun pause(hour: Float): Single<PatchBooleanResponse> {
        var h = (hour * 2).roundToInt()
        if (h > 4) h = 0xFF
        return writeAndRead(allocate().putByte(h).build())
    }
}

