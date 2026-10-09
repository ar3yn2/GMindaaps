package app.gmaaps.plugins.configuration.setupwizard.elements

import androidx.compose.runtime.Composable
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.protection.PasswordCheck
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.keys.interfaces.IntPreferenceKey
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.ui.compose.preference.AdaptiveIntPreferenceItem
import dev.zacsweers.metro.Inject

@Inject
class SWEditIntNumber(aapsLogger: AAPSLogger, rh: TextResolver, rxBus: RxBus, preferences: Preferences, passwordCheck: PasswordCheck) : SWItem(aapsLogger, rh, rxBus, preferences, passwordCheck) {

    private var updateDelay = 0

    fun preference(preference: IntPreferenceKey): SWEditIntNumber {
        this.preference = preference
        return this
    }

    fun updateDelay(updateDelay: Int): SWEditIntNumber {
        this.updateDelay = updateDelay
        return this
    }

    @Composable
    override fun Compose() {
        AdaptiveIntPreferenceItem(
            intKey = preference as IntPreferenceKey,
            title = label
        )
    }
}

