package app.gmaaps.pump.eopatch.core.response

import app.gmaaps.pump.eopatch.core.code.PatchBleResultCode

class BasalStopResponse(
    val id: Int,
    val unit_c: Int,
    val unit_w: Int,
    resultCode: PatchBleResultCode
) : BaseResponse(resultCode)

