package app.gmaaps.pump.medtrum.keys

import app.gmaaps.core.keys.interfaces.BooleanNonPreferenceKey

enum class MedtrumBooleanNonKey(
    override val key: String,
    override val defaultValue: Boolean,
) : BooleanNonPreferenceKey {

    PatchPrimed("patch_primed_flag", defaultValue = false),
}

