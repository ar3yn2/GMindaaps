package app.gmaaps.plugins.sync.xdrip.extensions

import app.gmaaps.core.data.model.GV
import org.json.JSONObject

fun GV.toXdripJson(): JSONObject =
    JSONObject()
        .put("device", sourceSensor.text)
        .put("mills", timestamp)
        .put("isValid", isValid)
        .put("mgdl", value)
        .put("direction", trendArrow.text)


