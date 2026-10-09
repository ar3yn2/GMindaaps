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
class InfoReminderSet(patch: IBleDevice, aapsLogger: AAPSLogger) : BaseBooleanAPI(PatchFunc.SET_INFO_REMINDER, patch, aapsLogger) {
    fun set(isInfoReminder: Boolean): Single<PatchBooleanResponse> =
        writeAndRead(allocate().putBoolean(isInfoReminder).putBoolean(false).build())
}

