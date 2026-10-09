package app.gmaaps.ui.compose.treatments.viewmodels

import app.gmaaps.core.data.pump.defs.PumpDescription
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.UserEntryLogger
import app.gmaaps.core.interfaces.maintenance.ImportExportPrefs
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.profile.ProfileFunction
import app.gmaaps.core.interfaces.profile.ProfileRepository
import app.gmaaps.core.interfaces.profile.ProfileUtil
import app.gmaaps.core.interfaces.pump.PumpWithConcentration
import app.gmaaps.core.interfaces.resources.ResourceHelper
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.interfaces.ui.UiInteraction
import app.gmaaps.core.interfaces.userEntry.UserEntryPresentationHelper
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.interfaces.utils.DecimalFormatter
import app.gmaaps.core.interfaces.utils.Translator
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class TreatmentsViewModelTest {

    @Mock private lateinit var persistenceLayer: PersistenceLayer
    @Mock private lateinit var profileUtil: ProfileUtil
    @Mock private lateinit var profileFunction: ProfileFunction
    @Mock private lateinit var activePlugin: ActivePlugin
    @Mock private lateinit var profileRepository: ProfileRepository
    @Mock private lateinit var rh: ResourceHelper
    @Mock private lateinit var translator: Translator
    @Mock private lateinit var dateUtil: DateUtil
    @Mock private lateinit var decimalFormatter: DecimalFormatter
    @Mock private lateinit var uiInteraction: UiInteraction
    @Mock private lateinit var userEntryPresentationHelper: UserEntryPresentationHelper
    @Mock private lateinit var importExportPrefs: ImportExportPrefs
    @Mock private lateinit var uel: UserEntryLogger
    @Mock private lateinit var aapsLogger: AAPSLogger
    @Mock private lateinit var rxBus: RxBus

    private val pump: PumpWithConcentration = mock()
    private val pumpDescription: PumpDescription = mock()

    private lateinit var sut: TreatmentsViewModel

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        whenever(activePlugin.activePump).thenReturn(pump)
        whenever(pump.pumpDescription).thenReturn(pumpDescription)
        sut = TreatmentsViewModel(
            persistenceLayer, profileUtil, profileFunction, activePlugin, profileRepository,
            rh, translator, dateUtil, decimalFormatter, uiInteraction, userEntryPresentationHelper,
            importExportPrefs, uel, aapsLogger, rxBus
        )
    }

    @Test
    fun `showExtendedBolusTab is true when pump is extended-bolus capable and not faking`() {
        whenever(pump.isFakingTempsByExtendedBoluses).thenReturn(false)
        whenever(pumpDescription.isExtendedBolusCapable).thenReturn(true)

        assertThat(sut.showExtendedBolusTab()).isTrue()
    }

    @Test
    fun `showExtendedBolusTab is false when pump fakes temps by extended boluses`() {
        whenever(pump.isFakingTempsByExtendedBoluses).thenReturn(true)
        whenever(pumpDescription.isExtendedBolusCapable).thenReturn(true)

        assertThat(sut.showExtendedBolusTab()).isFalse()
    }
}

