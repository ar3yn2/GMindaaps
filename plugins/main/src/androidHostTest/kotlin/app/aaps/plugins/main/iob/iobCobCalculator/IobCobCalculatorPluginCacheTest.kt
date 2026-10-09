package app.gmaaps.plugins.main.iob.iobCobCalculator

import app.gmaaps.core.interfaces.concurrent.withLock
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.db.ProcessedTbrEbData
import app.gmaaps.core.interfaces.notifications.NotificationManager
import app.gmaaps.core.interfaces.overview.OverviewData
import app.gmaaps.core.interfaces.overview.graph.OverviewDataCache
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.profile.EffectiveProfile
import app.gmaaps.core.interfaces.profile.ProfileFunction
import app.gmaaps.core.interfaces.pump.PumpWithConcentration
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.interfaces.utils.DecimalFormatter
import app.gmaaps.core.interfaces.workflow.CalculationSignalsEmitter
import app.gmaaps.core.interfaces.workflow.CalculationWorkflow
import app.gmaaps.core.keys.DoubleKey
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.shared.tests.AAPSLoggerTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking
import org.mockito.kotlin.whenever
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * The cached IOB of the past is kept when the BG data is loaded again: a new BG changes nothing in it.
 * Only values older than [IobCobCalculatorPlugin.CACHED_IOB_HOURS] are dropped, and [IobCobCalculatorPlugin.clearCache]
 * still drops everything.
 *
 * Each calculation reads the boluses once, so the number of those reads shows whether a value was
 * calculated or taken from the cache.
 */
class IobCobCalculatorPluginCacheTest {

    private val persistenceLayer = mock<PersistenceLayer>()
    private val activePlugin = mock<ActivePlugin>()
    private val dateUtil = mock<DateUtil>()
    private val preferences = mock<Preferences>()
    private val profile = mock<EffectiveProfile>()
    private lateinit var sut: IobCobCalculatorPlugin

    private val now = 1_800_000_000_000L
    private val hour = 60 * 60 * 1000L

    @BeforeEach
    fun setUp() = runBlocking {
        whenever(dateUtil.now()).thenReturn(now)
        whenever(preferences.get(DoubleKey.ApsAmaBolusSnoozeDivisor)).thenReturn(2.0)
        val pump = mock<PumpWithConcentration>()
        whenever(pump.isFakingTempsByExtendedBoluses).thenReturn(false)
        whenever(activePlugin.activePump).thenReturn(pump)
        whenever(persistenceLayer.getBolusesFromTime(any(), any())).thenReturn(emptyList())
        whenever(persistenceLayer.getTemporaryBasalsStartingFromTimeToTime(any(), any(), any())).thenReturn(emptyList())
        whenever(persistenceLayer.getExtendedBolusesStartingFromTimeToTime(any(), any(), any())).thenReturn(emptyList())
        sut = IobCobCalculatorPlugin(
            AAPSLoggerTest(), mock<RxBus>(), preferences, mock<TextResolver>(), mock<ProfileFunction>(), activePlugin, dateUtil,
            persistenceLayer, mock<OverviewData>(), mock<CalculationWorkflow>(), mock<DecimalFormatter>(), mock<ProcessedTbrEbData>(),
            mock<CalculationSignalsEmitter>(), mock<NotificationManager>()
        ) { mock<OverviewDataCache>() }
    }

    private fun calculateAt(time: Long) = runBlocking { sut.calculateFromTreatmentsAndTemps(time, profile) }

    private fun verifyCalculations(count: Int) = verifyBlocking(persistenceLayer, times(count)) { getBolusesFromTime(any(), any()) }

    @Test
    fun `a value of the past is calculated once and then taken from the cache`() {
        calculateAt(now - hour)
        calculateAt(now - hour)

        verifyCalculations(1)
    }

    @Test
    fun `a new BG keeps the cached values`() {
        calculateAt(now - hour)

        sut.bgDataReloaded()
        calculateAt(now - hour)

        verifyCalculations(1)
    }

    @Test
    fun `a new BG drops only the values older than the time the calculation looks back`() {
        val old = now - (IobCobCalculatorPlugin.CACHED_IOB_HOURS + 1) * hour
        val recent = now - hour
        calculateAt(old)
        calculateAt(recent)
        verifyCalculations(2)

        sut.bgDataReloaded()
        calculateAt(old)
        calculateAt(recent)

        // Only the old one again
        verifyCalculations(3)
    }

    @Test
    fun `clearCache still drops everything`() {
        calculateAt(now - hour)

        sut.clearCache()
        calculateAt(now - hour)

        verifyCalculations(2)
    }

    /**
     * A cached read must wait for a writer that holds the lock (#5211). The read used to skip the lock,
     * so on a cache hit it went straight through while another thread was changing the table.
     */
    @Test
    fun `a cached read waits for a writer holding the lock`() {
        calculateAt(now - hour) // now cached
        val readerStarted = CountDownLatch(1)
        val readerDone = AtomicBoolean(false)
        val reader = Thread {
            readerStarted.countDown()
            calculateAt(now - hour)
            readerDone.set(true)
        }

        sut.dataLock.withLock {
            reader.start()
            assertThat(readerStarted.await(5, TimeUnit.SECONDS)).isTrue()
            // Give it every chance to get through; it cannot while the lock is held here.
            Thread.sleep(200)
            assertThat(readerDone.get()).isFalse()
        }

        reader.join(5_000)
        assertThat(readerDone.get()).isTrue()
        verifyCalculations(1) // and it was still a cache hit
    }

    /** "Now" and the future are never stored: the running insulin is not complete yet. */
    @Test
    fun `now is calculated every time`() {
        calculateAt(now)
        sut.bgDataReloaded()
        calculateAt(now)

        verifyCalculations(2)
    }
}

