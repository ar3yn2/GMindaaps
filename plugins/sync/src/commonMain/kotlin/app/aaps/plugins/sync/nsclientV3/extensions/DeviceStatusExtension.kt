package app.gmaaps.plugins.sync.nsclientV3.extensions

import app.gmaaps.core.data.model.DS
import app.gmaaps.core.nssdk.localmodel.devicestatus.NSDeviceStatus
import kotlinx.serialization.json.Json

fun DS.toNSDeviceStatus(): NSDeviceStatus {
    val jsonParser = Json { ignoreUnknownKeys = true; encodeDefaults = true }
    val pump: NSDeviceStatus.Pump? = pump?.let { jsonParser.decodeFromString(it) }
    val openAps = NSDeviceStatus.OpenAps(
        suggested = suggested?.let { Json.decodeFromString(it) },
        enacted = enacted?.let { Json.decodeFromString(it) },
        iob = iob?.let { Json.decodeFromString(it) },
    )
    return NSDeviceStatus(
        date = timestamp,
        device = device,
        pump = pump,
        openaps = openAps,
        uploaderBattery = if (uploaderBattery != 0) uploaderBattery else null,
        isCharging = isCharging,
        uploader = null
    )
}

