package app.gmaaps.pump.virtual.keys

import app.gmaaps.core.keys.interfaces.BooleanNonPreferenceKey

enum class VirtualBooleanNonPreferenceKey(
    override val key: String,
    override val defaultValue: Boolean,
) : BooleanNonPreferenceKey {

    IsSuspended("virtual_pump_is_suspended", false),
}
