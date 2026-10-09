package app.gmaaps.pump.omnipod.common.bledriver.comm.legacy.io

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.pump.omnipod.common.bledriver.comm.OmnipodDashBleManagerImpl
import app.gmaaps.pump.omnipod.common.bledriver.comm.command.BleCommand
import app.gmaaps.pump.omnipod.common.bledriver.comm.command.BleCommandHello
import app.gmaaps.pump.omnipod.common.bledriver.comm.interfaces.io.BleConfirmError
import app.gmaaps.pump.omnipod.common.bledriver.comm.interfaces.io.BleConfirmIncorrectData
import app.gmaaps.pump.omnipod.common.bledriver.comm.interfaces.io.BleConfirmResult
import app.gmaaps.pump.omnipod.common.bledriver.comm.interfaces.io.BleConfirmSuccess
import app.gmaaps.pump.omnipod.common.bledriver.comm.interfaces.io.CharacteristicType
import app.gmaaps.pump.omnipod.common.bledriver.comm.legacy.callbacks.BleCommCallbacks
import java.util.concurrent.BlockingQueue
import app.gmaaps.pump.omnipod.common.bledriver.comm.interfaces.io.CmdBleIO as CmdBleIOInterface

class CmdBleIO(
    logger: AAPSLogger,
    characteristic: BluetoothGattCharacteristic,
    private val incomingPackets: BlockingQueue<ByteArray>,
    gatt: BluetoothGatt,
    bleCommCallbacks: BleCommCallbacks
) : BleIO(
    logger,
    characteristic,
    incomingPackets,
    gatt,
    bleCommCallbacks,
    CharacteristicType.CMD
), CmdBleIOInterface {

    override fun peekCommand(): ByteArray? {
        return incomingPackets.peek()
    }

    override fun hello() = sendAndConfirmPacket(BleCommandHello(OmnipodDashBleManagerImpl.CONTROLLER_ID).data)

    override fun expectCommandType(expected: BleCommand, timeoutMs: Long): BleConfirmResult {
        return receivePacket(timeoutMs)?.let {
            if (it.isNotEmpty() && it[0] == expected.data[0])
                BleConfirmSuccess
            else
                BleConfirmIncorrectData(it)
        }
            ?: BleConfirmError("Error reading packet")
    }
}

