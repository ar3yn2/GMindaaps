package app.gmaaps.pump.omnipod.dash.util

import app.gmaaps.core.interfaces.resources.ResourceHelper
import app.gmaaps.pump.omnipod.common.bledriver.comm.exceptions.FailedToConnectException
import app.gmaaps.pump.omnipod.common.bledriver.comm.exceptions.NotConnectedException
import app.gmaaps.pump.omnipod.common.bledriver.comm.exceptions.ScanException
import app.gmaaps.pump.omnipod.common.bledriver.comm.exceptions.ScanFailFoundTooManyException
import app.gmaaps.pump.omnipod.dash.R

class I8n {
    companion object {

        fun textFromException(exception: Throwable, rs: ResourceHelper): String {
            return when (exception) {
                is FailedToConnectException      -> rs.gs(R.string.omnipod_dash_failed_to_connect)
                is ScanFailFoundTooManyException -> rs.gs(R.string.omnipod_dash_found_too_many_pods)
                is ScanException                 -> rs.gs(R.string.omnipod_dash_scan_failed)
                is NotConnectedException         -> rs.gs(R.string.omnipod_dash_connection_lost)
                else                             ->
                    rs.gs(R.string.omnipod_dash_generic_error, exception.toString())
            }
        }
    }
}

