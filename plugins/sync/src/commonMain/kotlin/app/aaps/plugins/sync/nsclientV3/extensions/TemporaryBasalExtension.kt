package app.gmaaps.plugins.sync.nsclientV3.extensions

import app.gmaaps.core.data.model.IDs
import app.gmaaps.core.data.model.TB
import app.gmaaps.core.data.pump.defs.PumpType
import app.gmaaps.core.data.time.T
import app.gmaaps.core.interfaces.profile.Profile
import app.gmaaps.core.nssdk.localmodel.treatment.EventType
import app.gmaaps.core.nssdk.localmodel.treatment.NSTemporaryBasal
import app.gmaaps.core.objects.extensions.convertedToAbsolute

fun NSTemporaryBasal.toTemporaryBasal(): TB =
    TB(
        isValid = isValid,
        timestamp = date ?: throw IllegalArgumentException(),
        utcOffset = T.mins(utcOffset ?: 0L).msecs(),
        type = type.toType(),
        rate = rate,
        isAbsolute = isAbsolute,
        duration = duration,
        ids = IDs(nightscoutId = identifier, pumpId = pumpId, pumpType = PumpType.fromString(pumpType), pumpSerial = pumpSerial, endId = endId)
    )

fun NSTemporaryBasal.Type?.toType(): TB.Type =
    TB.Type.fromString(this?.name)

fun TB.toNSTemporaryBasal(profile: Profile): NSTemporaryBasal =
    NSTemporaryBasal(
        eventType = EventType.TEMPORARY_BASAL,
        isValid = isValid,
        date = timestamp,
        utcOffset = T.msecs(utcOffset).mins(),
        type = type.toType(),
        rate = convertedToAbsolute(timestamp, profile),
        isAbsolute = isAbsolute,
        absolute = if (isAbsolute) rate else null,
        percent = if (!isAbsolute) rate - 100 else null,
        duration = duration,
        identifier = ids.nightscoutId,
        pumpId = ids.pumpId,
        pumpType = ids.pumpType?.name,
        pumpSerial = ids.pumpSerial,
        endId = ids.endId
    )

fun TB.Type?.toType(): NSTemporaryBasal.Type =
    NSTemporaryBasal.Type.fromString(this?.name)

