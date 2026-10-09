package app.gmaaps.plugins.sync.nsclientV3.extensions

import app.gmaaps.core.data.model.EB
import app.gmaaps.core.data.model.IDs
import app.gmaaps.core.data.pump.defs.PumpType
import app.gmaaps.core.data.time.T
import app.gmaaps.core.interfaces.profile.Profile
import app.gmaaps.core.nssdk.localmodel.treatment.EventType
import app.gmaaps.core.nssdk.localmodel.treatment.NSExtendedBolus
import app.gmaaps.core.nssdk.localmodel.treatment.NSTreatment
import app.gmaaps.core.objects.extensions.toTemporaryBasal

fun NSExtendedBolus.toExtendedBolus(): EB =
    EB(
        isValid = isValid,
        timestamp = date ?: throw IllegalArgumentException(),
        utcOffset = T.mins(utcOffset ?: 0L).msecs(),
        amount = enteredinsulin,
        duration = duration,
        isEmulatingTempBasal = isEmulatingTempBasal == true,
        ids = IDs(nightscoutId = identifier, pumpId = pumpId, pumpType = PumpType.fromString(pumpType), pumpSerial = pumpSerial, endId = endId)
    )

fun EB.toNSExtendedBolus(profile: Profile, convertToTemporaryBasal: Boolean = true): NSTreatment =
    if (isEmulatingTempBasal && convertToTemporaryBasal)
        this.toTemporaryBasal(profile).toNSTemporaryBasal(profile).also {
            it.extendedEmulated = toNSExtendedBolus(profile, convertToTemporaryBasal = false) as NSExtendedBolus
        }
    else
        NSExtendedBolus(
            eventType = EventType.COMBO_BOLUS,
            isValid = isValid,
            date = timestamp,
            utcOffset = T.msecs(utcOffset).mins(),
            enteredinsulin = amount,
            duration = duration,
            isEmulatingTempBasal = isEmulatingTempBasal,
            rate = rate,
            identifier = ids.nightscoutId,
            pumpId = ids.pumpId,
            pumpType = ids.pumpType?.name,
            pumpSerial = ids.pumpSerial,
            endId = ids.endId
        )

