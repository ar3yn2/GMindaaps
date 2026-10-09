package app.gmaaps.core.keys

import app.gmaaps.core.keys.interfaces.IntComposedNonPreferenceKey

enum class IntComposedKey(
    override val key: String,
    override val format: String,
    override val defaultValue: Int,
) : IntComposedNonPreferenceKey {

    WidgetOpacity("appwidget_", "%d", 25)
}
