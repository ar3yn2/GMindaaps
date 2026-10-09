package app.gmaaps.pump.omnipod.common.bledriver.comm.legacy

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.pump.omnipod.common.bledriver.comm.exceptions.ConnectException
import app.gmaaps.pump.omnipod.common.bledriver.comm.interfaces.session.BleConnection
import app.gmaaps.pump.omnipod.common.bledriver.comm.interfaces.session.BleConnectionFactory
import app.gmaaps.pump.omnipod.common.bledriver.comm.legacy.session.Connection
import app.gmaaps.pump.omnipod.common.bledriver.pod.state.OmnipodDashPodStateManager
import dev.zacsweers.metro.Inject

@Inject
class LegacyBleConnectionFactory(
    private val context: Context,
    private val aapsLogger: AAPSLogger,
    private val config: Config,
    private val podState: OmnipodDashPodStateManager
) : BleConnectionFactory {

    private val bluetoothAdapter: BluetoothAdapter?
        get() = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager?)?.adapter

    override fun createConnection(podAddress: String): BleConnection {
        val adapter = bluetoothAdapter ?: throw ConnectException("Bluetooth not available")
        val podDevice = adapter.getRemoteDevice(podAddress)
        return Connection(podDevice, aapsLogger, config, context, podState)
    }
}

