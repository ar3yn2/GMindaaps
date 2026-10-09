package app.gmaaps.plugins.aps.openAPSSMB

import app.gmaaps.core.data.plugin.PluginType
import app.gmaaps.core.interfaces.bgQualityCheck.BgQualityCheck
import app.gmaaps.core.interfaces.constraints.ConstraintsChecker
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.iob.GlucoseStatusProvider
import app.gmaaps.core.interfaces.profiling.Profiler
import app.gmaaps.core.interfaces.stats.TddCalculator
import app.gmaaps.shared.tests.TestBaseWithProfile
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock

class OpenAPSSMBPluginTest : TestBaseWithProfile() {

    @Mock lateinit var constraintChecker: ConstraintsChecker
    @Mock lateinit var persistenceLayer: PersistenceLayer
    @Mock lateinit var glucoseStatusProvider: GlucoseStatusProvider
    @Mock lateinit var determineBasalSMB: DetermineBasalSMB
    @Mock lateinit var bgQualityCheck: BgQualityCheck
    @Mock lateinit var tddCalculator: TddCalculator
    @Mock lateinit var profiler: Profiler
    private lateinit var openAPSSMBPlugin: OpenAPSSMBPlugin

    @BeforeEach fun prepare() {
        openAPSSMBPlugin = OpenAPSSMBPlugin(
            aapsLogger, rxBus, constraintChecker, rh, profileFunction, profileUtil, config, activePlugin,
            iobCobCalculator, hardLimits, preferences, dateUtil, processedTbrEbData, persistenceLayer, glucoseStatusProvider,
            tddCalculator, bgQualityCheck, notificationManager, determineBasalSMB, profiler, GlucoseStatusCalculatorSMB(aapsLogger, iobCobCalculator, dateUtil, decimalFormatter, deltaCalculator), { apsResultProvider() }, ch,
            fabricPrivacy
        )
    }

    /** See the note in `OpenAPSAMAPluginTest`: construction is the coverage this class still carries. */
    @Test
    fun `the plugin constructs and is an APS`() {
        assertThat(openAPSSMBPlugin.getType()).isEqualTo(PluginType.APS)
    }
}

