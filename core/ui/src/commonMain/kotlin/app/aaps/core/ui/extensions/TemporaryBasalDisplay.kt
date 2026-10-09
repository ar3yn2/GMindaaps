package app.gmaaps.core.ui.extensions

import app.gmaaps.core.data.model.TB
import app.gmaaps.core.data.model.durationInMinutes
import app.gmaaps.core.data.model.getPassedDurationToTimeInMinutes
import app.gmaaps.core.interfaces.insulin.ConcentrationHelper
import app.gmaaps.core.interfaces.profile.Profile
import app.gmaaps.core.interfaces.pump.PumpRate
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.ui.CoreUiStrings

/**
 * Text for a temporary basal on screen.
 *
 * The maths that belongs to the record - `iobCalc`, `convertedToAbsolute`, `convertedToPercent` -
 * stays in `:core:objects`. Only the part that formats for a reader lives here, next to the strings
 * it uses. The time helpers it needs are in `:core:data` for that reason: `:core:ui` cannot depend on
 * `:core:objects`, which depends on this module.
 */
private fun TB.netExtendedRate(profile: Profile) = rate - profile.getBasal(timestamp)

fun TB.toStringFull(profile: Profile, dateUtil: DateUtil, rh: TextResolver): String {
    val timeAndDuration = "${dateUtil.timeString(timestamp)} ${getPassedDurationToTimeInMinutes(dateUtil.now())}/${durationInMinutes}'"

    return when {
        type == TB.Type.FAKE_EXTENDED -> rh.gs(CoreUiStrings.temp_basal_tsf_fake_extended, rate, netExtendedRate(profile), timeAndDuration)
        isAbsolute                    -> rh.gs(CoreUiStrings.temp_basal_tsf_absolute, rate, timeAndDuration)
        else                          -> rh.gs(CoreUiStrings.temp_basal_tsf_percent, rate, timeAndDuration)
    }
}

fun TB.toStringFull(profile: Profile, dateUtil: DateUtil, ch: ConcentrationHelper): String {
    val timeAndDuration = "${dateUtil.timeString(timestamp)} ${getPassedDurationToTimeInMinutes(dateUtil.now())}/${durationInMinutes}'"

    return when {
        type == TB.Type.FAKE_EXTENDED -> "${ch.basalRateString(PumpRate(rate), true)} (${netExtendedRate(profile)}E) $timeAndDuration"
        isAbsolute                    -> "${ch.basalRateString(PumpRate(rate), true)} $timeAndDuration"
        else                          -> "${ch.basalRateString(PumpRate(rate), false)} $timeAndDuration"
    }
}

fun TB.toStringShort(rh: TextResolver): String =
    if (isAbsolute || type == TB.Type.FAKE_EXTENDED) rh.gs(CoreUiStrings.pump_base_basal_rate, rate)
    else rh.gs(CoreUiStrings.formatPercent, rate)

