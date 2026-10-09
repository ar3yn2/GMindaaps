package app.gmaaps.plugins.automation.elements

import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.utils.MidnightUtils

class InputTimeRange(private val rh: TextResolver, private val dateUtil: DateUtil) {

    var start: Int = getMinSinceMidnight(dateUtil.now())
    var end: Int = getMinSinceMidnight(dateUtil.now())

    private fun getMinSinceMidnight(time: Long): Int = MidnightUtils.secondsFromMidnight(time) / 60
}

