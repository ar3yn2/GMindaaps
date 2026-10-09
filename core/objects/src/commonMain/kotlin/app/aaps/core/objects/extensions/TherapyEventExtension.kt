package app.gmaaps.core.objects.extensions

import app.gmaaps.core.data.model.GlucoseUnit
import app.gmaaps.core.data.model.IDs
import app.gmaaps.core.data.model.TE
import app.gmaaps.core.data.pump.defs.PumpType
import kotlin.time.Clock

fun TE.Companion.asAnnouncement(error: String, pumpId: Long? = null, pumpType: PumpType? = null, pumpSerial: String? = null): TE =
    TE(
        timestamp = Clock.System.now().toEpochMilliseconds(),
        type = TE.Type.ANNOUNCEMENT,
        duration = 0, note = error,
        enteredBy = "AAPS",
        glucose = null,
        glucoseType = null,
        glucoseUnit = GlucoseUnit.MGDL,
        ids = IDs(
            pumpId = pumpId,
            pumpType = pumpType,
            pumpSerial = pumpSerial
        )
    )

fun TE.Companion.asSettingsExport(error: String, pumpId: Long? = null, pumpType: PumpType? = null, pumpSerial: String? = null): TE =
    TE(
        timestamp = Clock.System.now().toEpochMilliseconds(),
        type = TE.Type.SETTINGS_EXPORT,
        duration = 0, note = error,
        enteredBy = "AAPS",
        glucose = null,
        glucoseType = null,
        glucoseUnit = GlucoseUnit.MGDL,
        ids = IDs(
            pumpId = pumpId,
            pumpType = pumpType,
            pumpSerial = pumpSerial
        )
    )

