package app.gmaaps.pump.eopatch.core.api

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.pump.eopatch.core.ble.BaseAPI
import app.gmaaps.pump.eopatch.core.ble.BytesConverter
import app.gmaaps.pump.eopatch.core.ble.PatchFunc
import app.gmaaps.pump.eopatch.core.response.FirmwareVersionResponse
import app.gmaaps.pump.eopatch.core.scan.IBleDevice
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.reactivex.rxjava3.core.Single

@SingleIn(AppScope::class)
@Inject
class GetFirmwareVersion(patch: IBleDevice, aapsLogger: AAPSLogger) : BaseAPI<FirmwareVersionResponse>(PatchFunc.GET_FIRMWARE_VERSION, patch, aapsLogger) {
    override fun parse(bytes: ByteArray): FirmwareVersionResponse {
        val success = bytes[DATA0].toInt() == 0
        if (!success) return FirmwareVersionResponse(false, 0, 0, 0, 0)
        return FirmwareVersionResponse(
            true,
            BytesConverter.toUInt(bytes[DATA1]), BytesConverter.toUInt(bytes[DATA2]),
            BytesConverter.toUInt(bytes[DATA3]), BytesConverter.toUInt(bytes[DATA6])
        )
    }

    fun get(): Single<FirmwareVersionResponse> = get(0x00)
    fun get(areaCode: Int): Single<FirmwareVersionResponse> = writeAndRead(allocate().putByte(RESERVED).putByte(areaCode).build())
}

