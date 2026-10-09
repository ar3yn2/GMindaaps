package app.gmaaps.pump.medtronic.data.dto

import app.gmaaps.core.utils.formatUS

/**
 * Created by andy on 6/2/18.
 */
open class PumpTimeStampedRecord(var atechDateTime: Long = 0) {

    var decimalPrecission = 2
    // var atechDateTime: Long = 0

    open fun getFormattedDecimal(value: Double): String? {
        return value.formatUS(decimalPrecission)
    }
}
