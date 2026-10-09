package app.gmaaps.plugins.sync.xdrip

import app.gmaaps.core.interfaces.aps.Loop
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.iob.GlucoseStatusProvider
import app.gmaaps.core.interfaces.sync.DataSyncSelectorXdrip
import app.gmaaps.plugins.sync.tidepool.utils.RateLimit
import app.gmaaps.plugins.sync.xdrip.compose.XdripMvvmRepository
import app.gmaaps.shared.tests.TestBaseWithProfile
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock
import org.mockito.kotlin.mock

class XdripPluginTest : TestBaseWithProfile() {

    @Mock lateinit var loop: Loop
    @Mock lateinit var glucoseStatusProvider: GlucoseStatusProvider
    @Mock lateinit var xdripMvvmRepository: XdripMvvmRepository
    @Mock lateinit var dataSyncSelector: DataSyncSelectorXdrip
    @Mock lateinit var persistenceLayer: PersistenceLayer

    private lateinit var xdripPlugin: XdripPlugin
    private lateinit var rateLimit: RateLimit

    @BeforeEach fun prepare() {
        rateLimit = RateLimit(dateUtil)
        xdripPlugin = XdripPlugin(
            aapsLogger,
            rh,
            preferences,
            profileFunction,
            profileUtil,
            context,
            fabricPrivacy,
            loop,
            iobCobCalculator,
            processedTbrEbData,
            rxBus,
            dateUtil,
            config,
            decimalFormatter,
            glucoseStatusProvider,
            xdripMvvmRepository,
            dataSyncSelector,
            persistenceLayer, mock()
        )
    }
}

