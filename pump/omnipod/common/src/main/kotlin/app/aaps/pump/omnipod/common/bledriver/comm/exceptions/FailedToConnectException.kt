package app.gmaaps.pump.omnipod.common.bledriver.comm.exceptions

open class FailedToConnectException(message: String? = null) : Exception("Failed to connect: ${message ?: ""}")

