package app.gmaaps.pump.danar.di

import app.gmaaps.core.interfaces.pump.rfcomm.RfcommTransport
import app.gmaaps.pump.dana.di.DanaAccessors
import app.gmaaps.pump.danar.DanaRPlugin
import app.gmaaps.pump.danarkorean.DanaRKoreanPlugin
import app.gmaaps.pump.danarv2.DanaRv2Plugin
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo

/**
 * What an instrumented test needs from the RFCOMM Dana drivers - see [DanaAccessors] for why these are
 * read from the app's graph rather than built by the test, and what contributing them costs.
 *
 * Extends [DanaAccessors] so a Dana R test gets the shared pieces from one cast. [rfcommTransport] is a
 * core type but a Dana binding: [DanaRTransportBindings] decides between the real transport and the
 * emulator, so this is where a test can see which one it got.
 */
@ContributesTo(AppScope::class)
interface DanaRAccessors : DanaAccessors {

    val danaRPlugin: DanaRPlugin
    val danaRKoreanPlugin: DanaRKoreanPlugin
    val danaRv2Plugin: DanaRv2Plugin
    val rfcommTransport: RfcommTransport
}

