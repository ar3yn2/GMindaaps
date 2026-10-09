package app.gmaaps.ui.compose.treatmentDialog

import app.gmaaps.core.data.pump.defs.PumpDescription
import app.gmaaps.core.interfaces.aps.Loop
import app.gmaaps.core.interfaces.bolus.BatchExecutor
import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.constraints.Constraint
import app.gmaaps.core.interfaces.constraints.ConstraintsChecker
import app.gmaaps.core.interfaces.insulin.ConcentrationHelper
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.profile.ProfileFunction
import app.gmaaps.core.interfaces.pump.PumpWithConcentration
import app.gmaaps.core.interfaces.resources.ResourceHelper
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.interfaces.utils.DecimalFormatter
import app.gmaaps.core.interfaces.utils.HardLimits
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@OptIn(ExperimentalCoroutinesApi::class)
internal class TreatmentDialogViewModelTest {

    @Mock private lateinit var constraintChecker: ConstraintsChecker
    @Mock private lateinit var activePlugin: ActivePlugin
    @Mock private lateinit var ch: ConcentrationHelper
    @Mock private lateinit var config: Config
    @Mock private lateinit var decimalFormatter: DecimalFormatter
    @Mock private lateinit var rh: ResourceHelper
    @Mock private lateinit var profileFunction: ProfileFunction
    @Mock private lateinit var hardLimits: HardLimits
    @Mock private lateinit var loop: Loop
    @Mock private lateinit var batchExecutor: BatchExecutor
    @Mock private lateinit var rxBus: RxBus

    private val pump: PumpWithConcentration = mock()
    private val pumpDescription: PumpDescription = mock()
    private val maxBolus: Constraint<Double> = mock()
    private val maxCarbs: Constraint<Int> = mock()

    private lateinit var sut: TreatmentDialogViewModel

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(StandardTestDispatcher())
        whenever(activePlugin.activePump).thenReturn(pump)
        whenever(pump.pumpDescription).thenReturn(pumpDescription)
        whenever(pumpDescription.bolusStep).thenReturn(0.1)
        whenever(maxBolus.value()).thenReturn(10.0)
        whenever(constraintChecker.getMaxBolusAllowed()).thenReturn(maxBolus)
        whenever(maxCarbs.value()).thenReturn(100)
        whenever(constraintChecker.getMaxCarbsAllowed()).thenReturn(maxCarbs)
        sut = TreatmentDialogViewModel(
            constraintChecker, activePlugin, ch, config, decimalFormatter, rh,
            profileFunction, hardLimits, loop, batchExecutor, rxBus, CoroutineScope(UnconfinedTestDispatcher())
        )
    }

    @AfterEach
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `updateInsulin and updateCarbs update the state`() {
        sut.updateInsulin(2.5)
        sut.updateCarbs(30)

        assertThat(sut.uiState.value.insulin).isEqualTo(2.5)
        assertThat(sut.uiState.value.carbs).isEqualTo(30)
    }
}

