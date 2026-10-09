package app.gmaaps.pump.dana.keys

import app.gmaaps.core.keys.interfaces.IntNonPreferenceKey

enum class DanaIntNonKey(
    override val key: String,
    override val defaultValue: Int,
) : IntNonPreferenceKey {

    Password("danar_password", 0),
}

