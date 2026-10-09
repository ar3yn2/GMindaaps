package app.gmaaps.plugins.sync.nsclientV3.extensions

import app.gmaaps.core.data.model.CAL
import app.gmaaps.core.data.model.IDs
import app.gmaaps.core.data.time.T
import app.gmaaps.core.nssdk.localmodel.entry.NSMbgV3
import app.gmaaps.core.nssdk.localmodel.entry.NsUnits

fun NSMbgV3.toCAL(): CAL =
    CAL(
        timestamp = date ?: throw IllegalArgumentException(),
        fingerstickMgdl = mbg,
        sensorMgdlAtPairing = sensorMgdlAtPairing,
        ids = IDs(nightscoutId = identifier),
        isValid = isValid,
        utcOffset = T.mins(utcOffset ?: 0L).msecs()
    )

fun CAL.toNSMbgV3(): NSMbgV3 =
    NSMbgV3(
        isValid = isValid,
        date = timestamp,
        utcOffset = T.msecs(utcOffset).mins(),
        mbg = fingerstickMgdl,
        sensorMgdlAtPairing = sensorMgdlAtPairing,
        units = NsUnits.MG_DL,
        identifier = ids.nightscoutId,
        isCalibration = true
    )

fun CAL.contentEqualsTo(other: CAL): Boolean =
    isValid == other.isValid &&
        timestamp == other.timestamp &&
        utcOffset == other.utcOffset &&
        fingerstickMgdl == other.fingerstickMgdl &&
        sensorMgdlAtPairing == other.sensorMgdlAtPairing

fun CAL.onlyNsIdAdded(previous: CAL): Boolean =
    previous.id != id &&
        contentEqualsTo(previous) &&
        previous.ids.nightscoutId == null &&
        ids.nightscoutId != null

