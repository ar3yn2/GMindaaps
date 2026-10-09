package app.gmaaps.pump.omnipod.common.bledriver.comm.message

import app.gmaaps.core.utils.toHex

class IncorrectPacketException(
    val payload: ByteArray,
    expectedIndex: Byte? = null
) : Exception("Invalid payload: ${payload.toHex()}. Expected index: $expectedIndex")

