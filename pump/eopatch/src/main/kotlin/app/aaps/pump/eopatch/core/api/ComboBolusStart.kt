package app.gmaaps.pump.eopatch.core.api

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.pump.eopatch.core.ble.BaseAPI
import app.gmaaps.pump.eopatch.core.ble.BytesConverter
import app.gmaaps.pump.eopatch.core.ble.PatchFunc
import app.gmaaps.pump.eopatch.core.ble.PumpCounter
import app.gmaaps.pump.eopatch.core.define.IPatchConstant.Companion.BOLUS_EXTENDED_DURATION_STEP
import app.gmaaps.pump.eopatch.core.define.IPatchConstant.Companion.EXT_BOLUS_ID
import app.gmaaps.pump.eopatch.core.define.IPatchConstant.Companion.NOW_BOLUS_ID
import app.gmaaps.pump.eopatch.core.response.ComboBolusStartResponse
import app.gmaaps.pump.eopatch.core.scan.IBleDevice
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.reactivex.rxjava3.core.Single

@SingleIn(AppScope::class)
@Inject
class ComboBolusStart(patch: IBleDevice, aapsLogger: AAPSLogger) : BaseAPI<ComboBolusStartResponse>(PatchFunc.START_COMBO_BOLUS, patch, aapsLogger) {
    override fun parse(bytes: ByteArray): ComboBolusStartResponse {
        if (bytes[DATA0].toInt() != 0) return ComboBolusStartResponse(false, 0, 0)
        val id = BytesConverter.toUInt(bytes[DATA1], bytes[DATA2])
        val extendedId = BytesConverter.toUInt(bytes[DATA3], bytes[DATA4])
        return ComboBolusStartResponse(true, id, extendedId)
    }

    fun start(bolusNowDoseU: Float, bolusExDoseU: Float, bolusExDurationMins: Int): Single<ComboBolusStartResponse> {
        val nowCount = PumpCounter.getPumpCountShort(bolusNowDoseU)
        val exCount = PumpCounter.getPumpCountShort(bolusExDoseU)
        val duration = (bolusExDurationMins / BOLUS_EXTENDED_DURATION_STEP).toByte()
        return writeAndRead(
            allocate().putShort(NOW_BOLUS_ID).putShort(nowCount).reserved()
                .putShort(EXT_BOLUS_ID).putShort(exCount).putByte(duration).build()
        )
    }
}

