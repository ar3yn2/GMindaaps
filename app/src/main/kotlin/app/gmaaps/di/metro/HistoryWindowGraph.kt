package app.gmaaps.di.metro

import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.db.ProcessedTbrEbData
import app.gmaaps.core.interfaces.iob.IobCobCalculator
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.notifications.NotificationManager
import app.gmaaps.core.interfaces.overview.OverviewData
import app.gmaaps.core.interfaces.overview.graph.OverviewDataCache
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.profile.ProfileFunction
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.interfaces.utils.DecimalFormatter
import app.gmaaps.core.interfaces.workflow.CalculationSignalsEmitter
import app.gmaaps.core.interfaces.workflow.CalculationWorkflow
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.objects.workflow.CalculationSignalsImpl
import app.gmaaps.implementation.overview.OverviewDataImpl
import app.gmaaps.plugins.main.iob.iobCobCalculator.IobCobCalculatorPlugin
import app.gmaaps.ui.compose.overview.OverviewDataCacheFactory
import dev.zacsweers.metro.GraphExtension
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

/**
 * Marks objects that belong to one history browsing window rather than to the running app.
 *
 * A separate scope, because that is the whole point: the History Browser recalculates over a different
 * time range and must not touch the loop's own calculation state while doing it.
 */
abstract class HistoryWindowScope private constructor()

/**
 * One history browsing window's calculation objects.
 *
 * This is the case that makes graph extensions worth having. It is an extension of [AppRootGraph] with
 * **its own scope**, so it inherits the app-wide objects - logger, database, preferences - while every
 * `@SingleIn(HistoryWindowScope::class)` binding below is its own. Ask the root for two windows and
 * they share the database and own separate calculators. `MetroScopingTest` checks exactly that
 * behaviour rather than trusting it.
 *
 * The bug this whole class exists to prevent is handing the window the app's own `OverviewData`:
 * history browsing would then write into the state the loop is running on.
 */
@GraphExtension(HistoryWindowScope::class)
interface HistoryWindowGraph {

    val overviewData: OverviewData
    val signals: CalculationSignalsEmitter
    val cache: OverviewDataCache
    val iobCobCalculator: IobCobCalculator

    /** Held by the root, so a caller can open a window whenever the user picks a day. */
    @GraphExtension.Factory
    fun interface Factory {

        fun create(): HistoryWindowGraph
    }

    @SingleIn(HistoryWindowScope::class)
    @Provides
    fun provideOverviewData(): OverviewData = OverviewDataImpl()

    @SingleIn(HistoryWindowScope::class)
    @Provides
    fun provideSignals(): CalculationSignalsEmitter = CalculationSignalsImpl()

    /**
     * The cache and the calculator need each other. A function type is a deferred lookup, so the
     * graph accepts it where a direct reference would be a cycle error.
     */
    @SingleIn(HistoryWindowScope::class)
    @Provides
    fun provideCache(
        factory: OverviewDataCacheFactory,
        signals: CalculationSignalsEmitter,
        iobCobCalculator: () -> IobCobCalculator
    ): OverviewDataCache = factory.create(
        iobCobCalculatorProvider = { iobCobCalculator() },
        signals = signals,
        // A history window reads a fixed range. Following live database changes is the live loop's job.
        observeDatabase = false
    )

    @Suppress("LongParameterList")
    @SingleIn(HistoryWindowScope::class)
    @Provides
    fun provideIobCobCalculator(
        aapsLogger: AAPSLogger,
        rxBus: RxBus,
        preferences: Preferences,
        rh: TextResolver,
        profileFunction: ProfileFunction,
        activePlugin: ActivePlugin,
        dateUtil: DateUtil,
        persistenceLayer: PersistenceLayer,
        overviewData: OverviewData,
        calculationWorkflow: CalculationWorkflow,
        decimalFormatter: DecimalFormatter,
        processedTbrEbData: ProcessedTbrEbData,
        signals: CalculationSignalsEmitter,
        notificationManager: NotificationManager,
        cache: () -> OverviewDataCache
    ): IobCobCalculator = IobCobCalculatorPlugin(
        aapsLogger, rxBus, preferences, rh, profileFunction, activePlugin, dateUtil, persistenceLayer,
        overviewData, calculationWorkflow, decimalFormatter, processedTbrEbData, signals, notificationManager
    ) { cache() }
}

