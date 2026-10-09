package app.gmaaps.plugins.sync.xdrip.keys

import app.gmaaps.core.keys.PreferenceType
import app.gmaaps.core.keys.interfaces.IntentPreferenceKey
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.plugins.sync.SyncStrings

enum class XdripIntentKey(
    override val key: String,
    override val title: TextRef,
    override val summary: TextRef? = null,
    override val preferenceType: PreferenceType = PreferenceType.CLICK,
    override val exportable: Boolean = false
) : IntentPreferenceKey {

    Info(
        key = "xdrip_info",
        title = SyncStrings.xdrip_local_broadcasts_title,
        summary = SyncStrings.xdrip_local_broadcasts_summary,
        preferenceType = PreferenceType.CLICK
    )
    ;

}

