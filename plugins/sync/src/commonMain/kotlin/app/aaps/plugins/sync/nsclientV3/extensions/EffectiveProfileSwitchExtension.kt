package app.gmaaps.plugins.sync.nsclientV3.extensions

import app.gmaaps.core.data.model.EPS
import app.gmaaps.core.data.model.ICfg
import app.gmaaps.core.data.model.IDs
import app.gmaaps.core.data.pump.defs.PumpType
import app.gmaaps.core.data.time.T
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.nssdk.localmodel.treatment.EventType
import app.gmaaps.core.nssdk.localmodel.treatment.NSEffectiveProfileSwitch
import app.gmaaps.core.nssdk.localmodel.treatment.NSICfg
import app.gmaaps.core.objects.extensions.pureProfileFromJson
import app.gmaaps.core.objects.profile.ProfileSealed

/** [insulinFallback] stamps records uploaded without one — see `NsIncomingDataProcessor.fallbackICfg`. */
fun NSEffectiveProfileSwitch.toEffectiveProfileSwitch(dateUtil: DateUtil, insulinFallback: ICfg): EPS? {
    val pureProfile = pureProfileFromJson(profileJson, dateUtil) ?: return null
    val profileSealed = ProfileSealed.Pure(value = pureProfile, activePlugin = null)
    val iCfg =
        iCfg?.let {
            ICfg(insulinLabel = it.insulinLabel, insulinEndTime = it.insulinEndTime, insulinPeakTime = it.insulinPeakTime, concentration = it.concentration)
        } ?: insulinFallback

    return EPS(
        isValid = isValid,
        timestamp = date ?: throw IllegalArgumentException(),
        utcOffset = T.mins(utcOffset ?: 0L).msecs(),
        basalBlocks = profileSealed.basalBlocks,
        isfBlocks = profileSealed.isfBlocks,
        icBlocks = profileSealed.icBlocks,
        targetBlocks = profileSealed.targetBlocks,
        glucoseUnit = profileSealed.units,
        originalProfileName = originalProfileName,
        originalCustomizedName = originalCustomizedName,
        originalTimeshift = originalTimeshift,
        originalPercentage = originalPercentage,
        originalDuration = originalDuration,
        originalEnd = originalEnd,
        originalPsId = originalPsId,
        iCfg = iCfg,
        ids = IDs(nightscoutId = identifier, pumpId = pumpId, pumpType = PumpType.fromString(pumpType), pumpSerial = pumpSerial, endId = endId)
    )
}

fun EPS.toNSEffectiveProfileSwitch(dateUtil: DateUtil): NSEffectiveProfileSwitch =
    NSEffectiveProfileSwitch(
        eventType = EventType.NOTE,
        isValid = isValid,
        date = timestamp,
        utcOffset = T.msecs(utcOffset).mins(),
        profileJson = ProfileSealed.EPS(value = this, activePlugin = null).toPureNsJson(dateUtil).toString(),
        originalProfileName = originalProfileName,
        originalCustomizedName = originalCustomizedName,
        originalTimeshift = originalTimeshift,
        originalPercentage = originalPercentage,
        originalDuration = originalDuration,
        originalEnd = originalEnd,
        originalPsId = originalPsId,
        notes = originalCustomizedName,
        identifier = ids.nightscoutId,
        pumpId = ids.pumpId,
        pumpType = ids.pumpType?.name,
        pumpSerial = ids.pumpSerial,
        endId = ids.endId,
        iCfg = NSICfg(insulinLabel = iCfg.insulinLabel, insulinEndTime = iCfg.insulinEndTime, insulinPeakTime = iCfg.insulinPeakTime, concentration = iCfg.concentration)
    )
