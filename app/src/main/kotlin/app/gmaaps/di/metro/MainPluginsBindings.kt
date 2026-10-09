package app.gmaaps.di.metro

import app.gmaaps.core.interfaces.constraints.Objectives
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.db.ProcessedTbrEbData
import app.gmaaps.core.interfaces.iob.IobCobCalculator
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.notifications.NotificationManager
import app.gmaaps.core.interfaces.overview.OverviewData
import app.gmaaps.core.interfaces.overview.graph.OverviewDataCache
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.plugin.PluginBase
import app.gmaaps.core.interfaces.profile.ProfileFunction
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.interfaces.utils.DecimalFormatter
import app.gmaaps.core.interfaces.workflow.CalculationSignalsEmitter
import app.gmaaps.core.interfaces.workflow.CalculationWorkflow
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.plugins.constraints.objectives.ObjectivesPlugin
import app.gmaaps.plugins.main.iob.iobCobCalculator.IobCobCalculatorPlugin
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntKey
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

/**
 * The live loop's `IobCobCalculatorPlugin`, replacing the hand-construction in `MainPluginsModule`.
 * ## Why the class is not simply annotated
 * `VirtualPumpPlugin` took `@Inject` and `@ContributesIntoMap` directly, which is tidier. This one
 * cannot: it is built **twice**, on purpose. `HistoryWindowGraph` builds a second one at
 * `HistoryWindowScope` over a fixed time range, so the history browser can calculate without disturbing
 * the live loop. A `@SingleIn(AppScope::class)` on the class would put a binding for the same type in
 * the parent of that extension, and which one a window saw would stop being obvious. Each graph naming
 * its own is what keeps the two apart - the shape `HistoryWindowGraph` already uses.
 * ## The cycle
 * The calculator and the cache need each other. [Provider] defers the lookup, so the graph accepts here
 * what a direct reference would reject, and `runCalculation` only ever runs after construction. The
 * plugin takes a plain lambda rather than a [Provider] because it is common code and Metro's own
 * `Provider` would be fine there, but the class predates this and the lambda costs nothing to adapt.
 */
@ContributesTo(AppScope::class)
@BindingContainer
object MainPluginsBindings {

    @Suppress("LongParameterList")
    @Provides
    @SingleIn(AppScope::class)
    fun iobCobCalculatorPlugin(
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
    ): IobCobCalculatorPlugin = IobCobCalculatorPlugin(
        aapsLogger, rxBus, preferences, rh, profileFunction, activePlugin, dateUtil, persistenceLayer,
        overviewData, calculationWorkflow, decimalFormatter, processedTbrEbData, signals, notificationManager
    ) { cache() }

    @Provides
    fun iobCobCalculator(plugin: IobCobCalculatorPlugin): IobCobCalculator = plugin

    @Provides
    @IntoMap
    @IntKey(10)
    fun iobCobCalculatorEntry(plugin: IobCobCalculatorPlugin): PluginBase = plugin

    /**
     * The `Objectives` interface, unqualified.
     *
     * `ObjectivesPlugin` carries `@APS` on the class for the plugin-list multibinding, and a second
     * `@ContributesBinding` there would inherit it - the interface would then only be readable as
     * `@APS Objectives`, which is not what a reader asks for.
     *
     * Metro documents the way out: put the qualifier on the bound type instead, `binding<@APS
     * PluginBase>()`. The version pinned here rejects that form outright -
     * `Inapplicable candidate(s): constructor(scope: KClass<*>, binding: binding<*> = ...)` - which is
     * the same wall `SyncPluginsBindings` hits for its qualified entry. So this stays stated, and
     * hands out the same scoped instance either way. Retry both when Metro leaves the snapshot.
     */
    @Provides
    fun objectives(plugin: ObjectivesPlugin): Objectives = plugin
}

