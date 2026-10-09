package app.gmaaps.core.objects.extensions

import app.gmaaps.core.keys.interfaces.BooleanNonPreferenceKey
import app.gmaaps.core.keys.interfaces.DoubleNonPreferenceKey
import app.gmaaps.core.keys.interfaces.IntNonPreferenceKey
import app.gmaaps.core.keys.interfaces.LongNonPreferenceKey
import app.gmaaps.core.keys.interfaces.NonPreferenceKey
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.keys.interfaces.StringNonPreferenceKey
import app.gmaaps.core.keys.interfaces.UnitDoublePreferenceKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map

/**
 * Observe changes of a [NonPreferenceKey] and emit [Unit] each time the value changes.
 * Skips the initial replay from the underlying [kotlinx.coroutines.flow.StateFlow] so subscribers
 * only react to actual changes.
 */
fun Preferences.observeChange(key: NonPreferenceKey): Flow<Unit> {
    val source: Flow<Any?> = when (key) {
        is BooleanNonPreferenceKey -> observe(key)
        is StringNonPreferenceKey  -> observe(key)
        is IntNonPreferenceKey     -> observe(key)
        is LongNonPreferenceKey    -> observe(key)
        is DoubleNonPreferenceKey  -> observe(key)
        is UnitDoublePreferenceKey -> observe(key)
        else                       -> error("Unsupported key type: ${key::class.simpleName}")
    }
    return source.drop(1).map { }
}

