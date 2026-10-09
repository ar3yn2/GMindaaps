package app.gmaaps.pump.medtrum.keys

import app.gmaaps.core.keys.PreferenceType
import app.gmaaps.core.keys.interfaces.PreferenceEnabledCondition
import app.gmaaps.core.keys.interfaces.StringPreferenceKey
import app.gmaaps.core.keys.interfaces.StringValidator
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.pump.medtrum.R

enum class MedtrumStringKey(
    override val key: String,
    override val defaultValue: String,
    private val titleResId: Int,
    private val summaryResId: Int? = null,
    override val preferenceType: PreferenceType = PreferenceType.TEXT_FIELD,
    private val entriesResIds: Map<String, Int> = emptyMap(),
    override val isPassword: Boolean = false,
    override val isPin: Boolean = false,
    override val enabledCondition: PreferenceEnabledCondition = PreferenceEnabledCondition.ALWAYS,
    override val validator: StringValidator = StringValidator.NONE
) : StringPreferenceKey {

    MedtrumAlarmSettings(
        key = "alarm_setting",
        defaultValue = "6",
        titleResId = R.string.alarm_setting_title,
        summaryResId = R.string.alarm_setting_summary,
        preferenceType = PreferenceType.LIST,
        entriesResIds = mapOf(
            "0" to R.string.alarm_setting_light_vibrate_beep,
            "1" to R.string.alarm_setting_light_vibrate,
            "2" to R.string.alarm_setting_light_beep,
            "3" to R.string.alarm_setting_light,
            "4" to R.string.alarm_setting_vibrate_beep,
            "5" to R.string.alarm_setting_vibrate,
            "6" to R.string.alarm_setting_beep,
            "7" to R.string.alarm_setting_silent
        )
    ),
    ;

    override val title: TextRef = TextRef.AndroidRes(titleResId)
    override val entries: Map<String, TextRef> = entriesResIds.mapValues { TextRef.AndroidRes(it.value) }
    override val summary: TextRef? = summaryResId?.let { TextRef.AndroidRes(it) }
}

