package app.gmaaps.pump.danars.compose

import android.content.Context
import app.gmaaps.core.data.model.EB
import app.gmaaps.core.data.model.TB
import app.gmaaps.core.data.ue.Action
import app.gmaaps.core.data.ue.Sources
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.insulin.ConcentrationHelper
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.UserEntryLogger
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.pump.PumpRate
import app.gmaaps.core.interfaces.pump.PumpWithConcentration
import app.gmaaps.core.interfaces.pump.ble.BleTransport
import app.gmaaps.core.interfaces.queue.CommandQueue
import app.gmaaps.core.interfaces.resources.ResourceHelper
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.interfaces.rx.events.EventInitializationChanged
import app.gmaaps.core.interfaces.rx.events.EventPumpStatusChanged
import app.gmaaps.core.interfaces.rx.events.EventQueueChanged
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.ui.compose.StatusLevel
import app.gmaaps.core.ui.compose.pump.PumpInfoRow
import app.gmaaps.pump.dana.DanaPump
import app.gmaaps.pump.dana.R
import app.gmaaps.pump.dana.events.EventDanaRNewStatus
import app.gmaaps.pump.dana.keys.DanaStringNonKey
import app.gmaaps.pump.danars.DanaRSPlugin
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import app.gmaaps.core.ui.R as CoreUiR

/**
 * Unit test for [DanaRSOverviewViewModel], the DanaRS variant of the shared Dana overview VM. It
 * extends [app.gmaaps.pump.dana.compose.DanaOverviewViewModel] and only overrides the variant-specific
 * hooks (BLE unpair + not-paired banner + pair/unpair management actions). The base state assembly is
 * covered in dana's DanaOverviewViewModelTest; here we drive construction through the RS subclass and
 * exercise its distinctive [DanaRSOverviewViewModel.performUnpair] BLE-bond clearing logic.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class DanaRSOverviewViewModelTest {

    @Mock private lateinit var aapsLogger: AAPSLogger
    @Mock private lateinit var rh: ResourceHelper
    @Mock private lateinit var rxBus: RxBus
    @Mock private lateinit var commandQueue: CommandQueue
    @Mock private lateinit var dateUtil: DateUtil
    @Mock private lateinit var activePlugin: ActivePlugin
    @Mock private lateinit var ch: ConcentrationHelper
    @Mock private lateinit var persistenceLayer: PersistenceLayer
    @Mock private lateinit var uel: UserEntryLogger
    @Mock private lateinit var preferences: Preferences
    @Mock private lateinit var context: Context

    private val danaPump: DanaPump = mock()
    private val activePump: PumpWithConcentration = mock()
    private val danaRSPlugin: DanaRSPlugin = mock()
    private val bleTransport: BleTransport = mock()

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())

        // pumpDataFlow field-initializer combines these non-null StateFlow getters -> must be stubbed.
        whenever(danaPump.lastConnectionFlow).thenReturn(MutableStateFlow(0L))
        whenever(danaPump.reservoirRemainingUnitsFlow).thenReturn(MutableStateFlow(0.0))
        whenever(danaPump.batteryRemainingFlow).thenReturn(MutableStateFlow<Int?>(null))
        whenever(danaPump.lastBolusTimeFlow).thenReturn(MutableStateFlow<Long?>(null))
        whenever(danaPump.lastBolusAmountFlow).thenReturn(MutableStateFlow<Double?>(null))
        whenever(danaPump.temporaryBasalToString()).thenReturn("")

        // rx / persistence wiring touched at construction
        whenever(rxBus.toFlow(EventPumpStatusChanged::class)).thenReturn(emptyFlow())
        whenever(rxBus.toFlow(EventQueueChanged::class)).thenReturn(emptyFlow())
        whenever(rxBus.toFlow(EventDanaRNewStatus::class)).thenReturn(emptyFlow())
        whenever(rxBus.toFlow(EventInitializationChanged::class)).thenReturn(emptyFlow())
        whenever(persistenceLayer.observeChanges(EB::class)).thenReturn(emptyFlow())
        whenever(persistenceLayer.observeChanges(TB::class)).thenReturn(emptyFlow())

        // formatting collaborators (called during buildUiState before the isConfigured branch)
        whenever(ch.basalRateString(any(), any(), any())).thenReturn("0.00 U/h")
        whenever(ch.fromPump(any(), any())).thenReturn(0.0)
        whenever(ch.insulinAmountString(any())).thenReturn("0 U")

        whenever(activePlugin.activePump).thenReturn(activePump)
        whenever(activePump.baseBasalRate).thenReturn(PumpRate(0.0))

        whenever(rh.gs(CoreUiR.string.refresh)).thenReturn("Refresh")
        whenever(rh.gs(CoreUiR.string.pump_history)).thenReturn("History")
        whenever(rh.gs(CoreUiR.string.pairing)).thenReturn("Pair")
        whenever(rh.gs(CoreUiR.string.pump_unpair)).thenReturn("Unpair")
        whenever(rh.gs(CoreUiR.string.battery_label)).thenReturn("Battery")
        whenever(rh.gs(CoreUiR.string.daily_units)).thenReturn("Daily units")
        whenever(rh.gs(CoreUiR.string.max_daily_units)).thenReturn("Max daily units")
        whenever(rh.gs(CoreUiR.string.base_basal_rate_label)).thenReturn("Base basal")
        whenever(rh.gs(CoreUiR.string.tempbasal_label)).thenReturn("Temp basal")
        whenever(rh.gs(CoreUiR.string.extended_bolus_label)).thenReturn("Extended bolus")
        whenever(rh.gs(R.string.basal_bolus_step)).thenReturn("Basal/bolus step")
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = DanaRSOverviewViewModel(
        aapsLogger, rh, rxBus, commandQueue, dateUtil, danaPump,
        activePlugin, ch, persistenceLayer, danaRSPlugin, uel, preferences, bleTransport, context
    )

    @Test
    fun notConfigured_hasNoInfoRows_andOffersPairing() {
        whenever(activePump.isConfigured()).thenReturn(false)
        whenever(activePump.isInitialized()).thenReturn(false)

        val state = createViewModel().uiState.value

        assertThat(state.infoRows).isEmpty()
        assertThat(state.statusBanner).isNull()
        assertThat(state.primaryActions.map { it.label }).containsExactly("Refresh", "History")
        assertThat(state.managementActions.map { it.label }).containsExactly("Pair")
    }

    @Test
    fun configured_buildsInfoRows_andOffersUnpair() {
        whenever(activePump.isConfigured()).thenReturn(true)
        whenever(activePump.isInitialized()).thenReturn(true)
        whenever(danaPump.serialNumber).thenReturn("")       // empty -> serial row skipped
        whenever(danaPump.batteryRemaining).thenReturn(40)   // 27..51 -> WARNING

        val state = createViewModel().uiState.value

        assertThat(state.infoRows).isNotEmpty()
        val batteryRow = state.infoRows.filterIsInstance<PumpInfoRow>().first { it.label == "Battery" }
        assertThat(batteryRow.level).isEqualTo(StatusLevel.WARNING)
        // configured -> RS management offers Unpair, not Pair
        assertThat(state.managementActions.map { it.label }).contains("Unpair")
    }

    @Test
    fun performUnpair_clearsPairingRemovesKeysAndResetsPump() {
        // blank MAC -> the bleTransport.adapter.removeBond branch is skipped
        whenever(preferences.get(DanaStringNonKey.MacAddress)).thenReturn("")

        createViewModel().performUnpair()

        verify(uel).log(Action.CLEAR_PAIRING_KEYS, Sources.Dana)
        verify(danaRSPlugin).clearPairing()
        verify(preferences).remove(DanaStringNonKey.MacAddress)
        verify(preferences).remove(DanaStringNonKey.RsName)
        verify(preferences).remove(DanaStringNonKey.EmulatorDeviceName)
        verify(bleTransport).updatePairingState(any())
        verify(danaRSPlugin).changePump()
    }
}

