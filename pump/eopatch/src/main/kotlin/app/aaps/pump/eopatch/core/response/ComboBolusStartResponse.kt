package app.gmaaps.pump.eopatch.core.response

import app.gmaaps.pump.eopatch.core.code.PatchBleResultCode

class ComboBolusStartResponse(
    success: Boolean,
    id: Int,
    val extendedId: Int
) : BolusResponse(if (success) PatchBleResultCode.SUCCESS else PatchBleResultCode.UNKNOWN_ERROR, id)

