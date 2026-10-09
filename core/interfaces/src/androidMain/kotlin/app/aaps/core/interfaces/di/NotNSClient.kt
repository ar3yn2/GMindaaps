package app.gmaaps.core.interfaces.di

import dev.zacsweers.metro.Qualifier

/**
 * Multibinding qualifier for plugins that are present in every build config except the NSClient
 * (AAPSClient) builds. Kept in :core:interfaces (like [PumpDriver]) so feature modules can
 * self-register their [app.gmaaps.core.interfaces.plugin.PluginBase] into the map without depending on :app.
 */
@Qualifier
annotation class NotNSClient

