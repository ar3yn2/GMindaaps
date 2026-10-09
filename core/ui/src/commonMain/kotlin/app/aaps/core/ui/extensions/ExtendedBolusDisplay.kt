package app.gmaaps.core.ui.extensions

import app.gmaaps.core.data.model.EB
import app.gmaaps.core.data.model.getPassedDurationToTimeInMinutes
import app.gmaaps.core.data.time.T
import app.gmaaps.core.interfaces.insulin.ConcentrationHelper
import app.gmaaps.core.interfaces.pump.PumpRate
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.ui.CoreUiStrings

/**
 * Text for an extended bolus on screen. The record's own maths stays in `:core:objects` - see
 * [TB.toStringFull] for why the split runs where it does.
 */
fun EB.toStringFull(dateUtil: DateUtil, rh: TextResolver): String =
    rh.gs(CoreUiStrings.extended_bolus_full, rate, dateUtil.timeString(timestamp), getPassedDurationToTimeInMinutes(dateUtil.now()), T.msecs(duration).mins())

fun EB.toStringFull(dateUtil: DateUtil, ch: ConcentrationHelper): String =
    "${ch.basalRateString(PumpRate(rate), true)} ${dateUtil.timeString(timestamp)} ${getPassedDurationToTimeInMinutes(dateUtil.now())}/${T.msecs(duration).mins()}"

fun EB.toStringMedium(dateUtil: DateUtil, rh: TextResolver): String =
    rh.gs(CoreUiStrings.extended_bolus_medium, rate, getPassedDurationToTimeInMinutes(dateUtil.now()), T.msecs(duration).mins())

