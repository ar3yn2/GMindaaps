package app.gmaaps.pump.omnipod.common.bledriver.comm.message

import app.gmaaps.core.utils.toHex

class CrcMismatchException(val expected: Long, actual: Long, val payload: ByteArray) :
    Exception("CRC mismatch. Actual: $actual. Expected: $expected. Payload: ${payload.toHex()}")

