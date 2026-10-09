package app.gmaaps.pump.eopatch.core.response

import app.gmaaps.pump.eopatch.core.code.PatchBleResultCode

class PumpDurationResponse(
    val durationS: Int,
    val durationL: Int,
    val durationM: Int
) : BaseResponse(PatchBleResultCode.SUCCESS)

