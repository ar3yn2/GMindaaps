package app.gmaaps.pump.dana.keys

import app.gmaaps.core.keys.PreferenceType
import app.gmaaps.core.keys.interfaces.IntPreferenceKey
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.pump.dana.R

enum class DanaIntKey(
    override val key: String,
    override val defaultValue: Int,
    private val titleResId: Int,
    override val min: Int = Int.MIN_VALUE,
    override val max: Int = Int.MAX_VALUE,
    override val preferenceType: PreferenceType = PreferenceType.TEXT_FIELD,
    private val entriesResIds: Map<Int, Int> = emptyMap(),
) : IntPreferenceKey {

    BolusSpeed(
        key = "danars_bolusspeed",
        defaultValue = 0,
        titleResId = app.gmaaps.core.ui.R.string.bolusspeed,
        preferenceType = PreferenceType.LIST,
        entriesResIds = mapOf(
            0 to R.string.bolus_speed_12,
            1 to R.string.bolus_speed_30,
            2 to R.string.bolus_speed_60
        )
    ),
    ;

    override val title: TextRef = TextRef.AndroidRes(titleResId)
    override val entries: Map<Int, TextRef> = entriesResIds.mapValues { TextRef.AndroidRes(it.value) }
}

