package app.gmaaps.core.keys

import app.gmaaps.core.keys.interfaces.BooleanComposedNonPreferenceKey

private const val LOCAL_PROFILE = "LocalProfile"

enum class ProfileComposedBooleanKey(
    override val key: String,
    override val format: String,
    override val defaultValue: Boolean,
) : BooleanComposedNonPreferenceKey {

    LocalProfileNumberedMgdl(LOCAL_PROFILE + "_mgdl_", "%d", false),
}

