package app.gmaaps.ui.compose.careDialog

import androidx.lifecycle.SavedStateHandle
import app.gmaaps.core.data.model.GlucoseUnit
import app.gmaaps.core.data.model.TE
import app.gmaaps.core.interfaces.bolus.BatchExecutor
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.iob.GlucoseStatusProvider
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.profile.ProfileFunction
import app.gmaaps.core.interfaces.profile.ProfileUtil
import app.gmaaps.core.interfaces.resources.ResourceHelper
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.interfaces.utils.Translator
import app.gmaaps.core.keys.interfaces.Preferences
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
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
internal class CareDialogViewModelTest {

    @Mock private lateinit var persistenceLayer: PersistenceLayer
    @Mock private lateinit var batchExecutor: BatchExecutor
    @Mock private lateinit var profileFunction: ProfileFunction
    @Mock private lateinit var profileUtil: ProfileUtil
    @Mock private lateinit var glucoseStatusProvider: GlucoseStatusProvider
    @Mock private lateinit var translator: Translator
    @Mock private lateinit var preferences: Preferences
    @Mock private lateinit var rh: ResourceHelper
    @Mock private lateinit var dateUtil: DateUtil
    @Mock private lateinit var aapsLogger: AAPSLogger

    private lateinit var sut: CareDialogViewModel

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(StandardTestDispatcher())
        whenever(profileFunction.getUnits()).thenReturn(GlucoseUnit.MGDL)
        whenever(profileUtil.fromMgdlToUnits(any(), any())).thenReturn(0.0)
        sut = CareDialogViewModel(
            SavedStateHandle(), persistenceLayer, batchExecutor, profileFunction, profileUtil,
            glucoseStatusProvider, translator, preferences, rh, dateUtil, aapsLogger,
            CoroutineScope(UnconfinedTestDispatcher())
        )
    }

    @AfterEach
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `updateBgValue sets the value and auto-switches the meter from sensor to finger`() {
        assertThat(sut.uiState.value.meterType).isEqualTo(TE.MeterType.SENSOR)

        sut.updateBgValue(120.0)

        assertThat(sut.uiState.value.bgValue).isEqualTo(120.0)
        assertThat(sut.uiState.value.meterType).isEqualTo(TE.MeterType.FINGER)
    }

    @Test
    fun `updateDuration and updateNotes update the state`() {
        sut.updateDuration(30.0)
        sut.updateNotes("check")

        assertThat(sut.uiState.value.duration).isEqualTo(30.0)
        assertThat(sut.uiState.value.notes).isEqualTo("check")
    }

    @Test
    fun `updateEventTime records the time and marks it changed`() {
        sut.updateEventTime(123_456L)

        assertThat(sut.uiState.value.eventTime).isEqualTo(123_456L)
        assertThat(sut.uiState.value.eventTimeChanged).isTrue()
    }
}

