package app.gmaaps.pump.omnipod.common.bledriver.comm.session

interface ConnectionStateChangeHandler {

    fun onConnectionStateChange(status: Int, newState: Int)
}

