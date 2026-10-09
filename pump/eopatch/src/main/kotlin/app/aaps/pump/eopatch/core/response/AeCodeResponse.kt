package app.gmaaps.pump.eopatch.core.response

import app.gmaaps.pump.eopatch.core.code.PatchAeCode

class AeCodeResponse(
    private val aeCodes: Set<PatchAeCode>,
    val alarmCount: Int
) : BaseResponse() {

    val alarmCodes: Set<PatchAeCode> get() = aeCodes.filterNotNull().toSet()
}

