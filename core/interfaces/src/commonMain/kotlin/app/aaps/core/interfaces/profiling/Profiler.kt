package app.gmaaps.core.interfaces.profiling

import app.gmaaps.core.interfaces.logging.LTag

interface Profiler {

    fun log(lTag: LTag, function: String, start: Long)
}
