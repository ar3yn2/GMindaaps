package app.gmaaps.pump.danar.di

import app.gmaaps.core.data.plugin.PluginType
import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.configuration.ConfigBuilder
import app.gmaaps.core.interfaces.configuration.ExternalOptions
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.pump.rfcomm.RfcommTransport
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.pump.dana.keys.DanaStringNonKey
import app.gmaaps.pump.danar.DanaRPlugin
import app.gmaaps.pump.danar.emulator.DanaRPumpEmulator
import app.gmaaps.pump.danar.emulator.DanaRPumpState
import app.gmaaps.pump.danar.emulator.DanaRVariant
import app.gmaaps.pump.danar.emulator.EmulatorRfcommTransport
import app.gmaaps.pump.danar.services.RealRfcommTransport
import app.gmaaps.pump.danarkorean.DanaRKoreanPlugin
import app.gmaaps.pump.danarv2.DanaRv2Plugin
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

/**
 * Picks the Dana R transport: the in-tree emulator when one of the Dana R emulator options is on,
 * otherwise the real RFCOMM one. The option also picks which of the three Dana R variants is emulated.
 *
 * Lives in the driver rather than in the app's `withPumps` source set, where it used to be. It can,
 * because `:pump:dana:danar:emulator` sits on `:pump:dana:common` rather than on the driver, so the
 * driver is free to depend on the emulator without a cycle. The app now names no Dana R type at all.
 */
@ContributesTo(AppScope::class)
@BindingContainer
object DanaRTransportBindings {

    @Provides
    @SingleIn(AppScope::class)
    fun provideRfcommTransport(
        config: Config,
        realRfcommTransport: RealRfcommTransport,
        aapsLogger: AAPSLogger,
        preferences: Preferences,
        configBuilder: ConfigBuilder,
        danaRPlugin: DanaRPlugin,
        danaRKoreanPlugin: DanaRKoreanPlugin,
        danaRv2Plugin: DanaRv2Plugin
    ): RfcommTransport {
        val variant = when {
            config.isEnabled(ExternalOptions.EMULATE_DANA_R)        -> DanaRVariant.DANA_R
            config.isEnabled(ExternalOptions.EMULATE_DANA_R_KOREAN) -> DanaRVariant.DANA_R_KOREAN
            config.isEnabled(ExternalOptions.EMULATE_DANA_R_V2)     -> DanaRVariant.DANA_R_V2
            else                                                    -> null
        }
        return if (variant != null) {
            // Auto-enable the matching plugin for the emulator variant.
            // The auto-switch in MsgInitConnStatusTime doesn't reliably change the active plugin
            // at runtime, so we ensure the correct plugin is active from the start.
            val targetPlugin = when (variant) {
                DanaRVariant.DANA_R        -> danaRPlugin
                DanaRVariant.DANA_R_KOREAN -> danaRKoreanPlugin
                DanaRVariant.DANA_R_V2     -> danaRv2Plugin
            }
            if (!targetPlugin.isEnabled()) {
                aapsLogger.debug(LTag.PUMP, "Emulator: auto-enabling ${targetPlugin.javaClass.simpleName} for variant $variant")
                for (plugin in listOf(danaRPlugin, danaRKoreanPlugin, danaRv2Plugin)) {
                    plugin.setPluginEnabled(PluginType.PUMP, plugin == targetPlugin)
                }
                configBuilder.storeSettings("EmulatorVariantAutoSwitch")
            }

            var name = preferences.get(DanaStringNonKey.EmulatorDeviceName)
            if (name.isEmpty()) {
                name = "DAN${String.format("%05d", (0..99999).random())}EM"
                preferences.put(DanaStringNonKey.EmulatorDeviceName, name)
            }
            EmulatorRfcommTransport(
                emulator = DanaRPumpEmulator(
                    state = DanaRPumpState(variant).apply { serialNumber = name },
                    aapsLogger = aapsLogger
                ),
                aapsLogger = aapsLogger,
                deviceName = name
            )
        } else {
            realRfcommTransport
        }
    }
}

