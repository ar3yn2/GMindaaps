package app.gmaaps.pump.omnipod.common.bledriver.comm.interfaces.session

import app.gmaaps.pump.omnipod.common.bledriver.comm.Ids
import app.gmaaps.pump.omnipod.common.bledriver.comm.message.MessageIO
import app.gmaaps.pump.omnipod.common.bledriver.comm.session.ConnectionState
import app.gmaaps.pump.omnipod.common.bledriver.comm.session.ConnectionWaitCondition
import app.gmaaps.pump.omnipod.common.bledriver.comm.session.DisconnectHandler
import app.gmaaps.pump.omnipod.common.bledriver.comm.session.EapSqn
import app.gmaaps.pump.omnipod.common.bledriver.comm.session.Session

/**
 * Abstraction for BLE GATT connection lifecycle.
 * Implemented by Bluetooth library-specific adapters.
 */
interface BleConnection : DisconnectHandler {

    val session: Session?
    val msgIO: MessageIO?

    fun connect(connectionWaitCond: ConnectionWaitCondition)
    fun disconnect(closeGatt: Boolean)
    fun connectionState(): ConnectionState
    fun establishSession(ltk: ByteArray, msgSeq: Byte, ids: Ids, eapSqn: ByteArray): EapSqn?

    companion object {
        const val DEFAULT_CONNECT_TIMEOUT_MS = 30000L
    }
}

