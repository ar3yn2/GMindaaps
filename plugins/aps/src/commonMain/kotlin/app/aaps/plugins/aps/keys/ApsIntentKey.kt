package app.gmaaps.plugins.aps.keys

import app.gmaaps.core.keys.PreferenceType
import app.gmaaps.core.keys.interfaces.IntentPreferenceKey
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.plugins.aps.ApsStrings

enum class ApsIntentKey(
    override val key: String,
    override val title: TextRef,
    override val summary: TextRef? = null,
    override val preferenceType: PreferenceType = PreferenceType.URL,
    // urlRef rather than urlResId: a resource id is an Android Int and means nothing off Android.
    override val urlRef: TextRef? = null,
    override val exportable: Boolean = false
) : IntentPreferenceKey {

    LinkToDocs(
        key = "aps_link_to_docs",
        title = ApsStrings.openapsama_link_to_preference_json_doc_txt,
        preferenceType = PreferenceType.URL,
        urlRef = ApsStrings.openapsama_link_to_preference_json_doc
    )
}

