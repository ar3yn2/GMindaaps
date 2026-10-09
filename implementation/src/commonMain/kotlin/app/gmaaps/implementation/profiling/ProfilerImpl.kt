package app.gmaaps.implementation.profiling

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.profiling.Profiler
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlin.time.Clock

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class ProfilerImpl(val aapsLogger: AAPSLogger) : Profiler {

    override fun log(lTag: LTag, function: String, start: Long) {
        val milliseconds = Clock.System.now().toEpochMilliseconds() - start
        aapsLogger.debug(lTag, ">>> $function <<< executed in $milliseconds milliseconds")
    }
}
