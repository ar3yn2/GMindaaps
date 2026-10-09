package app.gmaaps.plugins.sensitivity.extensions

import app.gmaaps.core.data.model.TE
import app.gmaaps.core.data.time.T

fun List<TE>.isTherapyEventEvent5minBack(time: Long): Boolean {
    for (event in this) {
        if (event.timestamp <= time && event.timestamp > time - T.mins(5).msecs()) {
            return true
        }
    }
    return false
}
