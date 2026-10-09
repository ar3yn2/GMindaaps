package app.gmaaps.pump.diaconn.keys

import app.gmaaps.core.keys.interfaces.IntentPreferenceKey
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.pump.diaconn.R

enum class DiaconnIntentKey(
    override val key: String,
    private val titleResId: Int,
    override val exportable: Boolean = false
) : IntentPreferenceKey {

    BtSelector(key = "diaconn_bt_selector", titleResId = R.string.selectedpump)
    ;

    override val title: TextRef = TextRef.AndroidRes(titleResId)
}

