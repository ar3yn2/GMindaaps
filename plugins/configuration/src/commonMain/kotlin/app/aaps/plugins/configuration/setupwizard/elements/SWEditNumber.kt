package app.gmaaps.plugins.configuration.setupwizard.elements

import androidx.compose.runtime.Composable
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.protection.PasswordCheck
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.keys.interfaces.DoublePreferenceKey
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.ui.compose.preference.AdaptiveDoublePreferenceItem
import dev.zacsweers.metro.Inject

@Inject
class SWEditNumber(aapsLogger: AAPSLogger, rh: TextResolver, rxBus: RxBus, preferences: Preferences, passwordCheck: PasswordCheck) : SWItem(aapsLogger, rh, rxBus, preferences, passwordCheck) {

    private var updateDelay = 0

    fun preference(preference: DoublePreferenceKey): SWEditNumber {
        this.preference = preference
        return this
    }

    fun updateDelay(updateDelay: Int): SWEditNumber {
        this.updateDelay = updateDelay
        return this
    }

    @Composable
    override fun Compose() {
        AdaptiveDoublePreferenceItem(
            doubleKey = preference as DoublePreferenceKey,
            title = label
        )
    }
}

