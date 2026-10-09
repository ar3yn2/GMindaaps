package app.gmaaps.plugins.sync.smsCommunicator.keys

import app.gmaaps.core.keys.BooleanKey
import app.gmaaps.core.keys.PreferenceType
import app.gmaaps.core.keys.interfaces.BooleanPreferenceKey
import app.gmaaps.core.keys.interfaces.IntentPreferenceKey
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.plugins.sync.SyncStrings

enum class SmsIntentKey(
    override val key: String,
    override val title: TextRef,
    override val summary: TextRef? = null,
    override val preferenceType: PreferenceType = PreferenceType.ACTIVITY,
    override val dependency: BooleanPreferenceKey? = null,
    override val exportable: Boolean = false
) : IntentPreferenceKey {

    OtpSetup(
        key = "smscommunicator_otp_setup",
        title = SyncStrings.smscommunicator_tab_otp_label,
        dependency = BooleanKey.SmsAllowRemoteCommands
    )
    ;

}

