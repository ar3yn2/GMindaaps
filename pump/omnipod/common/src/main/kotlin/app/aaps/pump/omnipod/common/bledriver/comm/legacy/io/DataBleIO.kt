package app.gmaaps.pump.omnipod.common.bledriver.comm.legacy.io

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.pump.omnipod.common.bledriver.comm.interfaces.io.CharacteristicType
import app.gmaaps.pump.omnipod.common.bledriver.comm.legacy.callbacks.BleCommCallbacks
import java.util.concurrent.BlockingQueue
import app.gmaaps.pump.omnipod.common.bledriver.comm.interfaces.io.DataBleIO as DataBleIOInterface

class DataBleIO(
    logger: AAPSLogger,
    characteristic: BluetoothGattCharacteristic,
    incomingPackets: BlockingQueue<ByteArray>,
    gatt: BluetoothGatt,
    bleCommCallbacks: BleCommCallbacks
) : BleIO(
    logger,
    characteristic,
    incomingPackets,
    gatt,
    bleCommCallbacks,
    CharacteristicType.DATA
), DataBleIOInterface

