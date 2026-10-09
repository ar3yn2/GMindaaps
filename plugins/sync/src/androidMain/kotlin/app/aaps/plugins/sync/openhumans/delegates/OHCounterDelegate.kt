package app.gmaaps.plugins.sync.openhumans.delegates

import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.plugins.sync.di.OpenHumansScope
import app.gmaaps.plugins.sync.openhumans.keys.OhLongKey
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlin.reflect.KProperty

@SingleIn(OpenHumansScope::class)
@Inject
internal class OHCounterDelegate internal constructor(
    private val preferences: Preferences
) {

    private var value = preferences.get(OhLongKey.Counter)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Long = value

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) {
        this.value = value
        preferences.put(OhLongKey.Counter, value)
    }
}
