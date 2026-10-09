package app.gmaaps.pump.eopatch.keys

import app.gmaaps.core.keys.PreferenceType
import app.gmaaps.core.keys.interfaces.IntPreferenceKey
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.pump.eopatch.R

enum class EopatchIntKey(
    override val key: String,
    override val defaultValue: Int,
    private val titleResId: Int,
    override val min: Int = Int.MIN_VALUE,
    override val max: Int = Int.MAX_VALUE,
    override val preferenceType: PreferenceType = PreferenceType.TEXT_FIELD,
    private val entriesResIds: Map<Int, Int> = emptyMap(),
) : IntPreferenceKey {

    LowReservoirReminder("eopatch_low_reservoir_reminders", 10, titleResId = R.string.low_reservoir, preferenceType = PreferenceType.LIST),
    ExpirationReminder("eopatch_expiration_reminders", 4, titleResId = R.string.patch_expiration_reminders, preferenceType = PreferenceType.LIST),
    ;

    override val title: TextRef = TextRef.AndroidRes(titleResId)
    override val entries: Map<Int, TextRef> = entriesResIds.mapValues { TextRef.AndroidRes(it.value) }
}

