package app.gmaaps.pump.eopatch.core.api

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.pump.eopatch.core.ble.BaseAPI
import app.gmaaps.pump.eopatch.core.ble.BytesConverter
import app.gmaaps.pump.eopatch.core.ble.PatchFunc
import app.gmaaps.pump.eopatch.core.ble.PumpCounter
import app.gmaaps.pump.eopatch.core.code.PatchBleResultCode
import app.gmaaps.pump.eopatch.core.define.IPatchConstant.Companion.NOW_BOLUS_ID
import app.gmaaps.pump.eopatch.core.response.BolusResponse
import app.gmaaps.pump.eopatch.core.scan.IBleDevice
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.reactivex.rxjava3.core.Single

@SingleIn(AppScope::class)
@Inject
class BolusStart(patch: IBleDevice, aapsLogger: AAPSLogger) : BaseAPI<BolusResponse>(PatchFunc.START_NOW_BOLUS, patch, aapsLogger) {
    override fun parse(bytes: ByteArray): BolusResponse {
        val resultCode = if (bytes[DATA0].toInt() != 0) PatchBleResultCode.BOLUS_OTHER_RUNNING else PatchBleResultCode.SUCCESS
        val id = BytesConverter.toUInt(bytes[DATA1], bytes[DATA2])
        return BolusResponse(resultCode, id)
    }

    fun start(bolusDoseU: Float): Single<BolusResponse> {
        val count = PumpCounter.getPumpCountShort(bolusDoseU)
        return writeAndRead(allocate().putShort(NOW_BOLUS_ID).putShort(count).build())
    }
}

