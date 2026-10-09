package app.gmaaps.pump.common.hw.rileylink.keys

import app.gmaaps.core.keys.interfaces.StringNonPreferenceKey

enum class RileyLinkStringKey(
    override val key: String,
    override val defaultValue: String,
) : StringNonPreferenceKey {

    Name("pref_rileylink_name", ""),
    MacAddress("pref_rileylink_mac_address", ""),
}

