package app.gmaaps.core.interfaces.pump.defs

enum class PumpDeviceState {

    NeverContacted,
    Sleeping,
    WakingUp,
    Active,
    ErrorWhenCommunicating,
    TimeoutWhenCommunicating,
    PumpUnreachable,
    InvalidConfiguration;
}
