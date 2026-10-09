package app.gmaaps.pump.eopatch.core.response

import app.gmaaps.pump.eopatch.core.code.PatchBleResultCode

class TempBasalFinishTimeResponse(
    val tempBasalFinishTime: Int,
    resultCode: PatchBleResultCode
) : BaseResponse(resultCode)

