package app.gmaaps.plugins.aps.autotune.keys

import app.gmaaps.core.keys.interfaces.StringNonPreferenceKey

enum class AutotuneStringKey(
    override val key: String,
    override val defaultValue: String,
) : StringNonPreferenceKey {

    AutotuneLastRun("key_autotune_last_run", ""),
}

