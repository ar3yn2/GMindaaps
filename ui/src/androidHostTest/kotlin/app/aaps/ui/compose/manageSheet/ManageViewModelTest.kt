package app.gmaaps.ui.compose.manageSheet

import app.gmaaps.core.data.model.EB
import app.gmaaps.core.data.model.TB
import app.gmaaps.core.interfaces.aps.Loop
import app.gmaaps.core.interfaces.bolus.BatchExecutor
import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.db.ProcessedTbrEbData
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.plugin.PluginBase
import app.gmaaps.core.interfaces.plugin.PluginDescription
import app.gmaaps.core.interfaces.profile.ProfileFunction
import app.gmaaps.core.interfaces.pump.Pump
import app.gmaaps.core.interfaces.resources.ResourceHelper
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.interfaces.rx.events.EventCustomActionsChanged
import app.gmaaps.core.interfaces.rx.events.EventInitializationChanged
import app.gmaaps.core.interfaces.sync.NsClient
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.keys.interfaces.VisibilityContext
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * A pump plugin is BOTH a [Pump] and a [PluginBase] in production. The VM casts
 * `activePlugin.activePumpInternal as PluginBase` at field init, so the returned mock must satisfy
 * both types. This test-local abstract combines them; Mockito/Objenesis bypasses the super-constructor,
 * so the `mock()` arguments are never evaluated.
 */
internal abstract class FakePumpPlugin : PluginBase(mock<PluginDescription>(), mock<AAPSLogger>(), mock<ResourceHelper>(), mock()), Pump

@OptIn(ExperimentalCoroutinesApi::class)
internal class ManageViewModelTest {

    @Mock private lateinit var rh: ResourceHelper
    @Mock private lateinit var activePlugin: ActivePlugin
    @Mock private lateinit var profileFunction: ProfileFunction
    @Mock private lateinit var loop: Loop
    @Mock private lateinit var config: Config
    @Mock private lateinit var processedTbrEbData: ProcessedTbrEbData
    @Mock private lateinit var persistenceLayer: PersistenceLayer
    @Mock private lateinit var rxBus: RxBus
    @Mock private lateinit var dateUtil: DateUtil
    @Mock private lateinit var preferences: Preferences
    @Mock private lateinit var batchExecutor: BatchExecutor
    @Mock private lateinit var nsClient: NsClient
    @Mock private lateinit var visibilityContext: VisibilityContext

    private lateinit var pumpPlugin: FakePumpPlugin
    private lateinit var sut: ManageViewModel

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        // StandardTestDispatcher DEFERS the init{}-launched refreshState() coroutine (no advanceUntilIdle),
        // so the uiState stays at ManageUiState defaults and we test the synchronous methods.
        Dispatchers.setMain(StandardTestDispatcher())
        // setupEventListeners() builds cold flow chains synchronously in init — every source must be non-null.
        whenever(persistenceLayer.observeChanges(EB::class)).thenReturn(emptyFlow())
        whenever(persistenceLayer.observeChanges(TB::class)).thenReturn(emptyFlow())
        whenever(rxBus.toFlow(EventInitializationChanged::class)).thenReturn(emptyFlow())
        whenever(rxBus.toFlow(EventCustomActionsChanged::class)).thenReturn(emptyFlow())
        whenever(nsClient.masterOrPairedClientFlow).thenReturn(MutableStateFlow(false))
        // Field init casts activePumpInternal (a Pump) to PluginBase — the fake satisfies both.
        pumpPlugin = mock()
        whenever(activePlugin.activePumpInternal).thenReturn(pumpPlugin)
        sut = ManageViewModel(
            rh, activePlugin, profileFunction, loop, config, processedTbrEbData, persistenceLayer,
            rxBus, dateUtil, preferences, batchExecutor, nsClient, visibilityContext,
            CoroutineScope(UnconfinedTestDispatcher())
        )
    }

    @AfterEach
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `default uiState exposes ManageUiState defaults and the cast pump plugin`() {
        val state = sut.uiState.value
        // refreshState() is deferred, so these are the untouched constructor defaults.
        assertThat(state.showMutatingActions).isTrue()
        assertThat(state.showPump).isTrue()
        assertThat(state.showTempTarget).isFalse()
        assertThat(state.showTempBasal).isFalse()
        assertThat(state.customActions).isEmpty()
        // The field-init cast succeeded and stored our fake.
        assertThat(state.pumpPlugin).isSameInstanceAs(pumpPlugin)
    }
}

