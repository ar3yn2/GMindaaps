package app.gmaaps.pump.omnipod.common.bledriver.comm.exceptions

import app.gmaaps.core.utils.toHex

class CouldNotParseMessageException(val payload: ByteArray) :
    Exception("Could not parse message payload: ${payload.toHex()}")

