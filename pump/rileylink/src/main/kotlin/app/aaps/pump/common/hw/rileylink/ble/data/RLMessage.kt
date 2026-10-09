package app.gmaaps.pump.common.hw.rileylink.ble.data

/**
 * Created by andy on 5/6/18.
 */
interface RLMessage {

    fun getTxData(): ByteArray
    fun isValid(): Boolean
}

