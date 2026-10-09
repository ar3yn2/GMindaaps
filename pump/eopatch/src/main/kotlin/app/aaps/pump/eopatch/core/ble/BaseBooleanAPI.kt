package app.gmaaps.pump.eopatch.core.ble

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.pump.eopatch.core.response.PatchBooleanResponse
import app.gmaaps.pump.eopatch.core.scan.IBleDevice

abstract class BaseBooleanAPI(func: PatchFunc, patch: IBleDevice, aapsLogger: AAPSLogger) :
    BaseAPI<PatchBooleanResponse>(func, patch, aapsLogger) {

    override fun parse(bytes: ByteArray): PatchBooleanResponse = checkReturnValueZero(bytes)

    protected fun checkReturnValueZero(bytes: ByteArray) = PatchBooleanResponse(bytes[DATA0] == 0.toByte())

    protected fun parseBoolean(success: Boolean) = PatchBooleanResponse(success)
}

