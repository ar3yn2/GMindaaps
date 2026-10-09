package app.gmaaps.pump.eopatch.core.response

import app.gmaaps.pump.eopatch.core.code.PatchBleResultCode

class PatchInternalSuspendTimeResponse(
    success: Boolean,
    private val totalSecond: Int
) : BaseResponse(if (success) PatchBleResultCode.SUCCESS else PatchBleResultCode.UNKNOWN_ERROR) {

    val totalSeconds: Long get() = totalSecond.toLong()
}

