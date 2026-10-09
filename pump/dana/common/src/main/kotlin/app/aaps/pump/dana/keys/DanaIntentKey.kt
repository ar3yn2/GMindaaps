package app.gmaaps.pump.dana.keys

import app.gmaaps.core.keys.interfaces.IntentPreferenceKey
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.pump.dana.R

enum class DanaIntentKey(
    override val key: String,
    private val titleResId: Int,
    override val exportable: Boolean = false
) : IntentPreferenceKey {

    BtSelector(key = "dana_rs_bt_selector", titleResId = R.string.selectedpump)
    ;

    override val title: TextRef = TextRef.AndroidRes(titleResId)
}

