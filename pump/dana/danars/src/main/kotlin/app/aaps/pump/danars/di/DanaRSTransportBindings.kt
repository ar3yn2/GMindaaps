package app.gmaaps.pump.danars.di

import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.configuration.ExternalOptions
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.notifications.NotificationManager
import app.gmaaps.core.interfaces.pump.ble.BleTransport
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.pump.dana.keys.DanaStringNonKey
import app.gmaaps.pump.danars.emulator.EmulatorBleTransport
import app.gmaaps.pump.danars.emulator.NotificationPumpDisplay
import app.gmaaps.pump.danars.encryption.EncryptionType
import app.gmaaps.pump.danars.services.BleTransportImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

/**
 * Picks the Dana RS transport: the in-tree emulator when one of the Dana RS emulator options is on,
 * otherwise the real BLE one. The option also picks the encryption the emulated pump uses.
 *
 * Lives in the driver rather than in the app's `withPumps` source set, where it used to be. It can,
 * because `:pump:dana:danars:emulator` sits on `:pump:dana:common` rather than on the driver, so the
 * driver is free to depend on the emulator without a cycle. The app now names no Dana RS type at all.
 */
@ContributesTo(AppScope::class)
@BindingContainer
object DanaRSTransportBindings {

    @Provides
    @SingleIn(AppScope::class)
    fun provideBleTransport(
        config: Config,
        bleTransportImpl: BleTransportImpl,
        notificationManager: NotificationManager,
        aapsLogger: AAPSLogger,
        preferences: Preferences
    ): BleTransport {
        val encryptionType = when {
            config.isEnabled(ExternalOptions.EMULATE_DANA_RS_V1) -> EncryptionType.ENCRYPTION_DEFAULT
            config.isEnabled(ExternalOptions.EMULATE_DANA_RS_V3) -> EncryptionType.ENCRYPTION_RSv3
            config.isEnabled(ExternalOptions.EMULATE_DANA_BLE5)  -> EncryptionType.ENCRYPTION_BLE5
            else                                                 -> null
        }
        return if (encryptionType != null) {
            EmulatorBleTransport(
                encryptionType = encryptionType,
                pumpDisplay = NotificationPumpDisplay(notificationManager),
                aapsLogger = aapsLogger,
                deviceNameProvider = {
                    var name = preferences.get(DanaStringNonKey.EmulatorDeviceName)
                    if (name.isEmpty()) {
                        name = "UHH${String.format("%05d", (0..99999).random())}TI"
                        preferences.put(DanaStringNonKey.EmulatorDeviceName, name)
                    }
                    name
                }
            ).also { it.pumpState.rejectTempBasal = config.isEnabled(ExternalOptions.EMULATE_DANA_RS_REJECT_TBR) }
        } else {
            bleTransportImpl
        }
    }
}

