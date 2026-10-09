package app.gmaaps.plugins.sync.xdrip.extensions

import app.gmaaps.core.data.model.EB
import app.gmaaps.core.data.model.TE
import app.gmaaps.core.data.time.T
import app.gmaaps.core.interfaces.profile.Profile
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.objects.extensions.toTemporaryBasal
import org.json.JSONObject

fun EB.toJson(isAdd: Boolean, profile: Profile?, dateUtil: DateUtil): JSONObject? =
    profile?.let {
        if (isEmulatingTempBasal)
            toTemporaryBasal(profile)
                .toJson(isAdd, profile, dateUtil)
                ?.put("extendedEmulated", toRealJson(isAdd, dateUtil))
        else toRealJson(isAdd, dateUtil)
    }

fun EB.toRealJson(isAdd: Boolean, dateUtil: DateUtil): JSONObject =
    JSONObject()
        .put("created_at", dateUtil.toISOString(timestamp))
        .put("enteredBy", "openaps://" + "AndroidAPS")
        .put("eventType", TE.Type.COMBO_BOLUS.text)
        .put("duration", T.msecs(duration).mins())
        .put("durationInMilliseconds", duration)
        .put("splitNow", 0)
        .put("splitExt", 100)
        .put("enteredinsulin", amount)
        .put("relative", rate)
        .put("isValid", isValid)
        .put("isEmulatingTempBasal", isEmulatingTempBasal)
        .also {
            if (ids.pumpId != null) it.put("pumpId", ids.pumpId)
            if (ids.endId != null) it.put("endId", ids.endId)
            if (ids.pumpType != null) it.put("pumpType", ids.pumpType!!.name)
            if (ids.pumpSerial != null) it.put("pumpSerial", ids.pumpSerial)
            if (isAdd && ids.nightscoutId != null) it.put("_id", ids.nightscoutId)
        }


