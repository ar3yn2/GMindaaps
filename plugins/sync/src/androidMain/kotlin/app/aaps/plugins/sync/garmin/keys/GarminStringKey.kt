package app.gmaaps.plugins.sync.garmin.keys

import app.gmaaps.core.keys.interfaces.StringPreferenceKey
import app.gmaaps.core.keys.interfaces.StringValidator
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.plugins.sync.SyncStrings

enum class GarminStringKey(
    override val key: String,
    override val defaultValue: String,
    override val title: TextRef,
    override val isPassword: Boolean = false,
    override val isPin: Boolean = false,
    override val validator: StringValidator = StringValidator.NONE
) : StringPreferenceKey {

    RequestKey(key = "garmin_aaps_key", defaultValue = "", title = SyncStrings.garmin_request_key),
    ;

}

