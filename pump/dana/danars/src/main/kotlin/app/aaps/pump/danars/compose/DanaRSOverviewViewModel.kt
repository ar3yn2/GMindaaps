package app.gmaaps.pump.danars.compose

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import app.gmaaps.core.data.ue.Action
import app.gmaaps.core.data.ue.Sources
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.insulin.ConcentrationHelper
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.UserEntryLogger
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.pump.ble.BleTransport
import app.gmaaps.core.interfaces.pump.ble.PairingState
import app.gmaaps.core.interfaces.pump.ble.PairingStep
import app.gmaaps.core.interfaces.queue.CommandQueue
import app.gmaaps.core.interfaces.resources.ResourceHelper
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.ui.compose.StatusLevel
import app.gmaaps.core.ui.compose.pump.ActionCategory
import app.gmaaps.core.ui.compose.pump.PumpAction
import app.gmaaps.core.ui.compose.pump.StatusBanner
import app.gmaaps.pump.dana.DanaPump
import app.gmaaps.pump.dana.R
import app.gmaaps.pump.dana.compose.DanaOverviewViewModel
import app.gmaaps.pump.dana.keys.DanaStringNonKey
import app.gmaaps.pump.danars.DanaRSPlugin
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey

// Registers itself: @ViewModelKey infers the key from the class. No graph entry, and deliberately
// unscoped so each screen gets its own.
@ContributesIntoMap(AppScope::class, binding = binding<ViewModel>())
@ViewModelKey
@Stable
@Inject
class DanaRSOverviewViewModel(
    aapsLogger: AAPSLogger,
    rh: ResourceHelper,
    rxBus: RxBus,
    commandQueue: CommandQueue,
    dateUtil: DateUtil,
    private val danaPump: DanaPump,
    activePlugin: ActivePlugin,
    ch: ConcentrationHelper,
    persistenceLayer: PersistenceLayer,
    private val danaRSPlugin: DanaRSPlugin,
    uel: UserEntryLogger,
    preferences: Preferences,
    private val bleTransport: BleTransport,
    context: Context
) : DanaOverviewViewModel(
    aapsLogger = aapsLogger,
    rh = rh,
    rxBus = rxBus,
    commandQueue = commandQueue,
    dateUtil = dateUtil,
    danaPump = danaPump,
    activePlugin = activePlugin,
    ch = ch,
    persistenceLayer = persistenceLayer,
    uel = uel,
    preferences = preferences,
    context = context
) {

    override fun performUnpair() {
        uel.log(Action.CLEAR_PAIRING_KEYS, Sources.Dana)
        val address = preferences.get(DanaStringNonKey.MacAddress)
        danaRSPlugin.clearPairing()
        preferences.remove(DanaStringNonKey.MacAddress)
        preferences.remove(DanaStringNonKey.RsName)
        preferences.remove(DanaStringNonKey.EmulatorDeviceName)
        if (address.isNotBlank()) bleTransport.adapter.removeBond(address)
        bleTransport.updatePairingState(PairingState(step = PairingStep.IDLE))
        danaRSPlugin.changePump() // resets mDeviceAddress/mDeviceName — makes isConfigured() return false
        rxTrigger.value = System.currentTimeMillis()
    }

    override fun errorBanner(isConfigured: Boolean, isInitialized: Boolean): StatusBanner? {
        if (!isConfigured) return null
        val address = preferences.get(DanaStringNonKey.MacAddress)
        if (address.isBlank() || bleTransport.adapter.isDeviceBonded(address)) return null
        return StatusBanner(rh.gs(app.gmaaps.core.ui.R.string.ble_not_supported_or_not_paired), StatusLevel.CRITICAL)
    }

    override fun buildManagementActions(pump: DanaPump, isInitialized: Boolean, isConfigured: Boolean): List<PumpAction> = buildList {
        // User Settings (only for RS pumps with non-legacy firmware)
        if (pump.hwModel != 1 && pump.protocol != 0x00) {
            add(
                PumpAction(
                    label = rh.gs(R.string.danar_user_options),
                    icon = Icons.Filled.Settings,
                    category = ActionCategory.MANAGEMENT,
                    visible = isInitialized,
                    onClick = { onUserSettingsClick() }
                )
            )
        }

        // Pair / Unpair
        if (isConfigured) {
            add(
                PumpAction(
                    label = rh.gs(app.gmaaps.core.ui.R.string.pump_unpair),
                    icon = Icons.Filled.Bluetooth,
                    category = ActionCategory.MANAGEMENT,
                    onClick = { onUnpairClick() }
                )
            )
        } else {
            add(
                PumpAction(
                    label = rh.gs(app.gmaaps.core.ui.R.string.pairing),
                    icon = Icons.Filled.Bluetooth,
                    category = ActionCategory.MANAGEMENT,
                    onClick = { onPairClick() }
                )
            )
        }
    }
}

