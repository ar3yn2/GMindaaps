package app.gmaaps.pump.diaconn.keys

import app.gmaaps.core.keys.interfaces.IntNonPreferenceKey

enum class DiaconnIntNonKey(
    override val key: String,
    override val defaultValue: Int,
) : IntNonPreferenceKey {

    ApsIncarnationNo("aps_incarnation_no", 65536),
    PumpSerialNo("pump_serial_no", 0),
}

