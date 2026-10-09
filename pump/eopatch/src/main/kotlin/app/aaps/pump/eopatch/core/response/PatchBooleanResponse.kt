package app.gmaaps.pump.eopatch.core.response

import app.gmaaps.pump.eopatch.core.code.PatchBleResultCode

class PatchBooleanResponse(success: Boolean) :
    BaseResponse(if (success) PatchBleResultCode.SUCCESS else PatchBleResultCode.UNKNOWN_ERROR) {

    override fun toString(): String = "BooleanResponse{resultCode=$resultCode}"
}

