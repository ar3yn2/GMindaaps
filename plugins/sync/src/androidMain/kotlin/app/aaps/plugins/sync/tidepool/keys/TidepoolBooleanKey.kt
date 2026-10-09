package app.gmaaps.plugins.sync.tidepool.keys

import app.gmaaps.core.keys.interfaces.BooleanPreferenceKey
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.plugins.sync.SyncStrings

enum class TidepoolBooleanKey(
    override val key: String,
    override val defaultValue: Boolean,
    override val title: TextRef,
    override val summary: TextRef? = null,
) : BooleanPreferenceKey {

    UseTestServers("tidepool_dev_servers", false, title = SyncStrings.title_tidepool_dev_servers, summary = SyncStrings.summary_tidepool_dev_servers),
    ;

}

