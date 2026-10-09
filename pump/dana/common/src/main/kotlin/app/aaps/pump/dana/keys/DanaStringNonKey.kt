package app.gmaaps.pump.dana.keys

import app.gmaaps.core.keys.interfaces.StringNonPreferenceKey

enum class DanaStringNonKey(
    override val key: String,
    override val defaultValue: String,
    override val exportable: Boolean = true
) : StringNonPreferenceKey {

    RName("danar_bt_name", ""),
    RsName("danars_name", ""),
    MacAddress("danars_address", ""),
    Password("danars_password", ""),
    EmulatorDeviceName("danars_emulator_device_name", "", exportable = false),
}

