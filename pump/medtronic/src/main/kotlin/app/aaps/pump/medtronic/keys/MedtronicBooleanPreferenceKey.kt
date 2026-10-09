package app.gmaaps.pump.medtronic.keys

import app.gmaaps.core.keys.interfaces.BooleanPreferenceKey
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.pump.medtronic.R

enum class MedtronicBooleanPreferenceKey(
    override val key: String,
    override val defaultValue: Boolean,
    private val titleResId: Int,
    private val summaryResId: Int? = null,
) : BooleanPreferenceKey {

    SetNeutralTemp(
        key = "set_neutral_temps",
        defaultValue = true,
        titleResId = R.string.set_neutral_temps_title,
        summaryResId = R.string.set_neutral_temps_summary
    ),
    ;

    override val title: TextRef = TextRef.AndroidRes(titleResId)
    override val summary: TextRef? = summaryResId?.let { TextRef.AndroidRes(it) }
}

