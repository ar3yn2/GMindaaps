package app.gmaaps.pump.equil.di

import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.configuration.ExternalOptions
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.pump.equil.ble.EquilBleTransport
import app.gmaaps.pump.equil.ble.EquilBleTransportImpl
import app.gmaaps.pump.equil.emulator.EquilEmulatorBleTransport
import app.gmaaps.pump.equil.emulator.EquilPumpEmulator
import app.gmaaps.pump.equil.keys.EquilStringKey
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

/**
 * Picks the Equil transport: the in-tree emulator when that option is on, otherwise the real BLE one.
 * `EquilBleTransportImpl` carries `@SingleIn(AppScope::class)` but no `@ContributesBinding` - the
 * binding for `EquilBleTransport` has to be this function, or the emulator branch would be bypassed.
 *
 * Lives in the driver rather than in the app's `withPumps` source set, where it used to be. It can,
 * because `:pump:equil:emulator` sits on `:pump:equil:protocol` rather than on the driver, so the
 * driver is free to depend on the emulator without a cycle. The app now names no Equil type at all.
 */
@ContributesTo(AppScope::class)
@BindingContainer
object EquilTransportBindings {

    @Provides
    @SingleIn(AppScope::class)
    fun provideEquilBleTransport(
        config: Config,
        equilBleTransportImpl: EquilBleTransportImpl,
        aapsLogger: AAPSLogger,
        preferences: Preferences
    ): EquilBleTransport {
        return if (config.isEnabled(ExternalOptions.EMULATE_EQUIL)) {
            val pumpEmulator = EquilPumpEmulator(aapsLogger = aapsLogger)
            EquilEmulatorBleTransport(
                emulator = pumpEmulator,
                aapsLogger = aapsLogger,
                serialNumberProvider = {
                    val sn = "A${String.format("%05d", (0..99999).random())}"
                    preferences.put(EquilStringKey.EmulatorDeviceName, "Equil - $sn")
                    aapsLogger.debug(LTag.PUMPCOMM, "Equil emulator scan with serial: $sn")
                    sn
                },
                storedPasswordProvider = {
                    // Read the device password stored by CmdPair after pairing
                    val pwd = preferences.get(EquilStringKey.Password)
                    if (pwd.isNotEmpty()) pwd else null
                }
            )
        } else {
            equilBleTransportImpl
        }
    }
}

