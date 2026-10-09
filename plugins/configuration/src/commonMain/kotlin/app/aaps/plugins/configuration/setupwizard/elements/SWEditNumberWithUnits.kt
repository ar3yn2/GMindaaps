package app.gmaaps.plugins.configuration.setupwizard.elements

import androidx.compose.runtime.Composable
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.profile.ProfileUtil
import app.gmaaps.core.interfaces.protection.PasswordCheck
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.keys.interfaces.UnitDoublePreferenceKey
import app.gmaaps.core.ui.compose.preference.AdaptiveUnitDoublePreferenceItem
import dev.zacsweers.metro.Inject

@Inject
class SWEditNumberWithUnits(aapsLogger: AAPSLogger, rh: TextResolver, rxBus: RxBus, preferences: Preferences, passwordCheck: PasswordCheck, private val profileUtil: ProfileUtil) :
    SWItem(aapsLogger, rh, rxBus, preferences, passwordCheck) {

    private var updateDelay = 0

    fun preference(preference: UnitDoublePreferenceKey): SWEditNumberWithUnits {
        this.preference = preference
        return this
    }

    fun updateDelay(updateDelay: Int): SWEditNumberWithUnits {
        this.updateDelay = updateDelay
        return this
    }

    @Composable
    override fun Compose() {
        AdaptiveUnitDoublePreferenceItem(
            unitKey = preference as UnitDoublePreferenceKey,
            title = label
        )
    }
}

