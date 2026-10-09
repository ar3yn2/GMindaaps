package app.gmaaps.core.ui.compose

import androidx.compose.runtime.Composable
import app.gmaaps.core.interfaces.resources.TextRefValueRegistry
import app.gmaaps.core.interfaces.resources.formatTemplate
import app.gmaaps.core.keys.interfaces.TextRef

/**
 * Compose text on the desktop JVM, read from the generated English string maps.
 *
 * The shell registers each module it depends on with [TextRefValueRegistry] before anything is
 * drawn, exactly as `MainApp.registerStringOwners()` registers id maps on Android.
 *
 * Falls back to the name when a lookup fails, which happens when the shell has not registered the
 * owning module or when a `TextRef.Named` was built by hand instead of taken from the generated
 * object. Showing the name puts the fault on screen rather than showing an empty label.
 *
 * [TextRef.AndroidRes] holds a number that means nothing here. Shared code should not produce one,
 * so it renders as `?` - visible, and pointing at the caller that needs converting to a name.
 */
@Composable
actual fun stringResource(ref: TextRef): String = when (ref) {
    is TextRef.Literal    -> ref.text
    is TextRef.Named      -> formatTemplate(TextRefValueRegistry.textOf(ref) ?: ref.name, ref.args)
    is TextRef.AndroidRes -> "?"
}

