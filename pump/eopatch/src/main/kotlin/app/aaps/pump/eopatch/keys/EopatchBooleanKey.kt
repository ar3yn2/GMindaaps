package app.gmaaps.pump.eopatch.keys

import app.gmaaps.core.keys.interfaces.BooleanPreferenceKey
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.pump.eopatch.R

enum class EopatchBooleanKey(
    override val key: String,
    override val defaultValue: Boolean,
    private val titleResId: Int,
) : BooleanPreferenceKey {

    BuzzerReminder("eopatch_patch_buzzer_reminders", false, titleResId = R.string.patch_buzzer_reminders),
    ;

    override val title: TextRef = TextRef.AndroidRes(titleResId)
}

