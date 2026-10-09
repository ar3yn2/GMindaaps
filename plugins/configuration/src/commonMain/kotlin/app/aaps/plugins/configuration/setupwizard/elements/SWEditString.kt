package app.gmaaps.plugins.configuration.setupwizard.elements

import androidx.compose.runtime.Composable
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.protection.PasswordCheck
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.keys.interfaces.StringPreferenceKey
import app.gmaaps.core.ui.compose.preference.InlineStringPreferenceItem
import dev.zacsweers.metro.Inject

@Inject
class SWEditString(aapsLogger: AAPSLogger, rh: TextResolver, rxBus: RxBus, preferences: Preferences, passwordCheck: PasswordCheck) : SWItem(aapsLogger, rh, rxBus, preferences, passwordCheck) {

    private var validator: ((string: String) -> Boolean)? = null
    private var updateDelay = 0L

    fun preference(preference: StringPreferenceKey): SWEditString {
        this.preference = preference
        return this
    }

    fun validator(validator: (string: String) -> Boolean): SWEditString {
        this.validator = validator
        return this
    }

    fun updateDelay(updateDelay: Long): SWEditString {
        this.updateDelay = updateDelay
        return this
    }

    @Composable
    override fun Compose() {
        InlineStringPreferenceItem(
            stringKey = preference as StringPreferenceKey,
            title = label
        )
    }
}

