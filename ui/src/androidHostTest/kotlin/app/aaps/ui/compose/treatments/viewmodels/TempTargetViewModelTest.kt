package app.gmaaps.ui.compose.treatments.viewmodels

import app.gmaaps.core.data.model.TT
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.profile.ProfileUtil
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.shared.tests.generatedTextResolver
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
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
internal class TempTargetViewModelTest {

    @Mock private lateinit var persistenceLayer: PersistenceLayer
    @Mock private lateinit var profileUtil: ProfileUtil
    @Mock private lateinit var dateUtil: DateUtil
    @Mock private lateinit var aapsLogger: AAPSLogger
    @Mock private lateinit var rxBus: RxBus

    private lateinit var sut: TempTargetViewModel

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        whenever(persistenceLayer.observeChanges(TT::class)).thenReturn(emptyFlow())
        sut = TempTargetViewModel(persistenceLayer, profileUtil, generatedTextResolver(), dateUtil, aapsLogger, rxBus)
    }

    @AfterEach
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `default uiState is not in removing mode and has no selection`() {
        val state = sut.uiState.value
        assertThat(state.isRemovingMode).isFalse()
        assertThat(state.selectedItems).isEmpty()
        assertThat(state.tempTargets).isEmpty()
        assertThat(state.showInvalidated).isFalse()
    }

    @Test
    fun `enterSelectionMode selects the item and enables removing mode`() {
        val item = mock<TT>()

        sut.enterSelectionMode(item)

        assertThat(sut.uiState.value.isRemovingMode).isTrue()
        assertThat(sut.uiState.value.selectedItems).containsExactly(item)
    }

    @Test
    fun `exitSelectionMode clears selection`() {
        sut.enterSelectionMode(mock<TT>())

        sut.exitSelectionMode()

        assertThat(sut.uiState.value.isRemovingMode).isFalse()
        assertThat(sut.uiState.value.selectedItems).isEmpty()
    }

    @Test
    fun `toggleSelection adds then removes an item`() {
        val first = mock<TT>()
        val second = mock<TT>()
        sut.enterSelectionMode(first)

        sut.toggleSelection(second)
        assertThat(sut.uiState.value.selectedItems).containsExactly(first, second)

        sut.toggleSelection(second)
        assertThat(sut.uiState.value.selectedItems).containsExactly(first)
    }

    @Test
    fun `toggleInvalidated flips the flag`() {
        sut.toggleInvalidated()
        assertThat(sut.uiState.value.showInvalidated).isTrue()
    }

    @Test
    fun `getDeleteConfirmationMessage empty when nothing selected, plural for many`() {
        assertThat(sut.getDeleteConfirmationMessage()).isEqualTo("")

        sut.enterSelectionMode(mock<TT>())
        sut.toggleSelection(mock<TT>())
        assertThat(sut.getDeleteConfirmationMessage()).isEqualTo("Are you sure you want to remove 2 items")
    }
}

