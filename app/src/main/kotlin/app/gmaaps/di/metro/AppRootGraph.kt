package app.gmaaps.di.metro

import android.content.Context
import android.content.SharedPreferences
import androidx.work.WorkManager
import app.gmaaps.core.interfaces.alerts.LocalAlertUtils
import app.gmaaps.core.interfaces.aps.APSResult
import app.gmaaps.core.interfaces.aps.AutosensData
import app.gmaaps.core.interfaces.aps.Loop
import app.gmaaps.core.interfaces.automation.Automation
import app.gmaaps.core.interfaces.autotune.Autotune
import app.gmaaps.core.interfaces.bgQualityCheck.BgQualityCheck
import app.gmaaps.core.interfaces.bolus.BatchExecutor
import app.gmaaps.core.interfaces.bolus.WizardBolusExecutor
import app.gmaaps.core.interfaces.bolus.WizardExecutor
import app.gmaaps.core.interfaces.clientcontrol.ClientControlActionDispatcher
import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.configuration.ConfigBuilder
import app.gmaaps.core.interfaces.configuration.RunningConfigurationKeys
import app.gmaaps.core.interfaces.constraints.ConstraintsChecker
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.db.ProcessedTbrEbData
import app.gmaaps.core.interfaces.di.APS
import app.gmaaps.core.interfaces.di.ApplicationScope
import app.gmaaps.core.interfaces.di.FeatureMemberInjectors
import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.di.NotNSClient
import app.gmaaps.core.interfaces.di.PumpDriver
import app.gmaaps.core.interfaces.dst.DstHelper
import app.gmaaps.core.interfaces.insulin.ConcentrationHelper
import app.gmaaps.core.interfaces.insulin.InsulinManager
import app.gmaaps.core.interfaces.iob.GlucoseStatusProvider
import app.gmaaps.core.interfaces.iob.IobCobCalculator
import app.gmaaps.core.interfaces.local.LocaleDependentSetting
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.L
import app.gmaaps.core.interfaces.logging.LoggerUtils
import app.gmaaps.core.interfaces.logging.UserEntryLogger
import app.gmaaps.core.interfaces.maintenance.CloudDirectoryManager
import app.gmaaps.core.interfaces.maintenance.CloudStorageProvider
import app.gmaaps.core.interfaces.maintenance.FileListProvider
import app.gmaaps.core.interfaces.maintenance.ImportExportPrefs
import app.gmaaps.core.interfaces.maintenance.Maintenance
import app.gmaaps.core.interfaces.notifications.AlarmSoundPlayer
import app.gmaaps.core.interfaces.notifications.NotificationHolder
import app.gmaaps.core.interfaces.notifications.NotificationManager
import app.gmaaps.core.interfaces.nsclient.NSClientRepository
import app.gmaaps.core.interfaces.nsclient.ProcessedDeviceStatusData
import app.gmaaps.core.interfaces.nsclient.StoreDataForDb
import app.gmaaps.core.interfaces.overview.LastBgData
import app.gmaaps.core.interfaces.overview.OverviewData
import app.gmaaps.core.interfaces.overview.graph.GraphConfigRepository
import app.gmaaps.core.interfaces.overview.graph.OverviewDataCache
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.plugin.PermissionProvider
import app.gmaaps.core.interfaces.plugin.PluginBase
import app.gmaaps.core.interfaces.plugin.PluginPermissions
import app.gmaaps.core.interfaces.profile.ProfileFunction
import app.gmaaps.core.interfaces.profile.ProfileRepository
import app.gmaaps.core.interfaces.profile.ProfileStore
import app.gmaaps.core.interfaces.profile.ProfileUtil
import app.gmaaps.core.interfaces.profiling.Profiler
import app.gmaaps.core.interfaces.protection.ExportPasswordDataStore
import app.gmaaps.core.interfaces.protection.PasswordCheck
import app.gmaaps.core.interfaces.protection.ProtectionCheck
import app.gmaaps.core.interfaces.protection.SecureEncrypt
import app.gmaaps.core.interfaces.pump.BlePreCheck
import app.gmaaps.core.interfaces.pump.BolusProgressData
import app.gmaaps.core.interfaces.pump.DetailedBolusInfoStorage
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.pump.PumpStatusProvider
import app.gmaaps.core.interfaces.pump.PumpSync
import app.gmaaps.core.interfaces.pump.PumpWithConcentration
import app.gmaaps.core.interfaces.pump.TemporaryBasalStorage
import app.gmaaps.core.interfaces.queue.CommandQueue
import app.gmaaps.core.interfaces.receivers.ReceiverStatusStore
import app.gmaaps.core.interfaces.resources.ResourceHelper
import app.gmaaps.core.interfaces.rx.AapsSchedulers
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.interfaces.scenes.ActiveSceneSync
import app.gmaaps.core.interfaces.scenes.SceneActions
import app.gmaaps.core.interfaces.scenes.SceneAutomationApi
import app.gmaaps.core.interfaces.scenes.SceneChainResolver
import app.gmaaps.core.interfaces.scenes.SceneIconResolver
import app.gmaaps.core.interfaces.scenes.SceneStore
import app.gmaaps.core.interfaces.scenes.Scenes
import app.gmaaps.core.interfaces.sharedPreferences.SP
import app.gmaaps.core.interfaces.source.NSClientSource
import app.gmaaps.core.interfaces.stats.DexcomTirCalculator
import app.gmaaps.core.interfaces.stats.TddCalculator
import app.gmaaps.core.interfaces.stats.TirCalculator
import app.gmaaps.core.interfaces.storage.Storage
import app.gmaaps.core.interfaces.sync.DataSyncSelectorXdrip
import app.gmaaps.core.interfaces.sync.NsClient
import app.gmaaps.core.interfaces.sync.XDripBroadcast
import app.gmaaps.core.interfaces.ui.CarbSuggestionActions
import app.gmaaps.core.interfaces.ui.IconsProvider
import app.gmaaps.core.interfaces.ui.UiInteraction
import app.gmaaps.core.interfaces.userEntry.UserEntryPresentationHelper
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.interfaces.utils.DecimalFormatter
import app.gmaaps.core.interfaces.utils.HardLimits
import app.gmaaps.core.interfaces.utils.Translator
import app.gmaaps.core.interfaces.utils.TrendCalculator
import app.gmaaps.core.interfaces.utils.fabric.FabricPrivacy
import app.gmaaps.core.interfaces.versionChecker.VersionCheckerUtils
import app.gmaaps.core.interfaces.widget.WidgetUpdater
import app.gmaaps.core.interfaces.workflow.CalculationSignals
import app.gmaaps.core.interfaces.workflow.CalculationSignalsEmitter
import app.gmaaps.core.interfaces.workflow.CalculationWorkflow
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.keys.interfaces.VisibilityContext
import app.gmaaps.core.nssdk.interfaces.RunningConfiguration
import app.gmaaps.core.objects.crypto.CryptoUtil
import app.gmaaps.core.objects.di.CoreObjectsGraph
import app.gmaaps.core.objects.runningMode.RunningModeGuard
import app.gmaaps.core.objects.wizard.BolusWizard
import app.gmaaps.core.objects.wizard.QuickWizard
import app.gmaaps.core.utils.receivers.DataInbox
import app.gmaaps.database.AppRepository
import app.gmaaps.database.di.DatabaseConfig
import app.gmaaps.di.ExternalOptionsOverride
import app.gmaaps.implementation.lifecycle.ProcessLifecycleListener
import app.gmaaps.implementation.maintenance.cloud.CloudStorageManager
import app.gmaaps.implementation.notifications.SnackbarNotificationFallback
import app.gmaaps.implementation.plugin.PluginStore
import app.gmaaps.implementation.profile.ProfileSwitchExpiryScheduler
import app.gmaaps.implementation.profile.ProfileSwitchSilentGate
import app.gmaaps.implementation.resources.ResourceHelperImpl
import app.gmaaps.implementation.scenes.ActiveSceneManager
import app.gmaaps.implementation.scenes.SceneExecutor
import app.gmaaps.implementation.utils.fabric.FabricPrivacyImpl
import app.gmaaps.plugins.aps.loop.runningMode.RunningModeExpiryJob
import app.gmaaps.plugins.aps.loop.runningMode.RunningModeReconciler
import app.gmaaps.plugins.aps.openAPS.DeltaCalculator
import app.gmaaps.plugins.aps.openAPSAMA.DetermineBasalAMA
import app.gmaaps.plugins.aps.openAPSAutoISF.DetermineBasalAutoISF
import app.gmaaps.plugins.aps.openAPSAutoISF.GlucoseStatusCalculatorAutoIsf
import app.gmaaps.plugins.aps.openAPSSMB.DetermineBasalSMB
import app.gmaaps.plugins.aps.openAPSSMB.GlucoseStatusCalculatorSMB
import app.gmaaps.plugins.automation.AutomationRuntime
import app.gmaaps.plugins.automation.services.LastLocationDataContainer
import app.gmaaps.plugins.constraints.bgQualityCheck.BgQualityCheckPlugin
import app.gmaaps.plugins.constraints.dstHelper.DstHelperPlugin
import app.gmaaps.plugins.constraints.objectives.ObjectivesPlugin
import app.gmaaps.plugins.constraints.objectives.objectives.Objective
import app.gmaaps.plugins.constraints.signatureVerifier.SignatureVerifierPlugin
import app.gmaaps.plugins.source.DexcomPlugin
import app.gmaaps.plugins.source.NSClientSourcePlugin
import app.gmaaps.plugins.source.XdripSourcePlugin
import app.gmaaps.plugins.source.di.SourceMetroGraph
import app.gmaaps.plugins.sync.di.OpenHumansMetroBridge
import app.gmaaps.plugins.sync.nsclientV3.NSClientV3Plugin
import app.gmaaps.plugins.sync.nsclientV3.NsIncomingDataProcessor
import app.gmaaps.plugins.sync.nsclientV3.ReceiverDelegate
import app.gmaaps.plugins.sync.nsclientV3.clientcontrol.AuthorizedClientsRepository
import app.gmaaps.plugins.sync.nsclientV3.clientcontrol.ClientControlPublisher
import app.gmaaps.plugins.sync.nsclientV3.clientcontrol.ClientPairingRepository
import app.gmaaps.plugins.sync.nsclientV3.clientcontrol.PairingOfferFetcher
import app.gmaaps.plugins.sync.nsclientV3.clientcontrol.PairingOfferPublisher
import app.gmaaps.plugins.sync.smsCommunicator.SmsCommunicatorPlugin
import app.gmaaps.plugins.sync.smsCommunicator.compose.SmsCommunicatorRepository
import app.gmaaps.plugins.sync.tidepool.auth.AuthFlowOut
import app.gmaaps.plugins.sync.tidepool.comm.TidepoolUploader
import app.gmaaps.plugins.sync.tidepool.compose.TidepoolRepository
import app.gmaaps.plugins.sync.tidepool.utils.RateLimit
import app.gmaaps.plugins.sync.wear.WearPlugin
import app.gmaaps.plugins.sync.xdrip.compose.XdripMvvmRepository
import app.gmaaps.ui.activityMonitor.ActivityMonitor
import app.gmaaps.ui.search.BuiltInSearchables
import app.gmaaps.workflow.WorkflowChainData
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes
import dev.zacsweers.metro.MembersInjector
import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.MetroViewModelMultibindings
import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KClass

/**
 * The one Metro root. Everything else hangs off it as a graph extension.
 *
 * **There is exactly one root, and that is the safety property.** Several root graphs would be unsafe
 * by construction: two roots that both scope the same type get one instance each, silently.
 * `MetroScopingTest` shows exactly that - two roots built from the same factory share nothing at all.
 * A duplicated calculator or command queue is not a cosmetic problem in this app.
 *
 * With one root that cannot happen: a scoped binding lives here once and every extension sees the same
 * instance. An extension that declares its own scope gets its own instances of what it scopes, which is
 * what the history browser needs and what the same test verifies.
 */
/**
 * Pump accessors are NOT a supertype here. They used to be: a `PumpAccessors` interface in
 * `src/withPumps` named the Dana and Equil types so instrumented tests could reach the app's own
 * instances at compile time, with an empty copy in `src/aapsclient` so this file still compiled for a
 * follower. But `src/withPumps` is part of the `full` and `pumpcontrol` MAIN source sets, so that made
 * those pump modules a compile-time dependency of the app itself - removing `:pump:equil` from
 * `settings.gradle` failed `:app:compileFullDebugKotlin` and produced no APK at all.
 *
 * Each pump module now declares its own accessor interface and contributes it, so the accessors exist
 * exactly when their module does and nothing here names a pump. See `DanaAccessors` in
 * `:pump:dana:common` for what that costs - a test resolves them with a cast rather than at compile
 * time, and their accessors become roots that every `AppScope` graph on the same classpath has to
 * satisfy.
 */
@DependencyGraph(AppScope::class)
interface AppRootGraph : MetroViewModelMultibindings {

    // Both of the graphs below are extensions rather than roots of their own. A second
    // `@DependencyGraph(AppScope::class)` is unsafe: two graphs both declaring `AppScope` get a
    // separate copy of anything scoped there, and nothing reports it. As extensions they share this
    // graph's bindings instead of restating them, so their factories take no arguments at all.

    /** Workers, which WorkManager builds through `MetroWorkerFactory`. */
    val workersGraph: AppWorkersGraph

    /**
     * Opens one history browsing window. A factory rather than an accessor, because each window is a
     * separate scope with its own calculation objects - see [HistoryWindowGraph].
     */
    val historyWindowFactory: HistoryWindowGraph.Factory

    /**
     * The shared wizard and running-mode objects, from a binding container in `:core:objects`.
     *
     * A binding container, not a `@GraphExtension`: an extension can see its parent, but the parent
     * cannot see the extension, so anything contributed here could not depend on a wizard. As a
     * container its bindings are part of this graph, and it lives in commonMain where the Apple
     * targets can include it too.
     */
    val runningModeGuard: RunningModeGuard
    val quickWizard: QuickWizard
    val bolusWizard: BolusWizard

    /**
     * Plugins that register themselves with @ContributesIntoMap rather than being listed in a graph.
     *
     * Smoothing, sensitivity and calibration are on this shape: their plugins carry the binding on the
     * class, so those modules have no DI file at all. As other modules follow, their entries land here.
     */
    val contributedPlugins: Map<Int, PluginBase>

    /**
     * Contributed plugins that only belong in a build that runs the loop.
     *
     * The qualifier is the whole point: `:app` merges this bucket only when `config.APS`. Objectives,
     * the signature verifier and the storage constraint have no meaning in a build that never makes a
     * decision, and a plugin in the wrong bucket fails silently - a plugin list is just a list.
     */
    @APS
    val contributedApsPlugins: Map<Int, PluginBase>

    /** Contributed plugins that must NOT appear in a follower build. */
    @NotNSClient
    val contributedNotNsClientPlugins: Map<Int, PluginBase>

    /**
     * Pump drivers, which only a build with pump support merges.
     *
     * Safe to declare here even though this file is compiled for follower builds: the map is keyed on
     * `Int` and holds [PluginBase], so no pump type is named. A follower has no pump module on its
     * classpath, nothing contributes, and the map is empty - which is the correct answer for a follower.
     *
     * `@Multibinds(allowEmpty = true)` is what makes that legal. Without it Metro reports "no binding
     * found" for the follower flavours, because a map with no contributions has no binding at all.
     */
    @Multibinds(allowEmpty = true)
    @PumpDriver
    val contributedPumpDriverPlugins: Map<Int, PluginBase>

    /**
     * Member injectors contributed by feature modules, for classes the app constructs by hand.
     *
     * The four extensions below the root each carry their own such map, but an extension has to be named
     * by [MetroGraphs] to be read - and that file is compiled for follower builds, where no pump module
     * exists. A map declared here instead can be filled from anywhere with `@ContributesTo`, and nothing
     * pump-specific appears in its type, so a follower simply sees fewer entries.
     *
     * How the pump protocol classes are member-injected: a
     * packet is built with `new`, then fills its own fields from this map.
     */
    @Multibinds(allowEmpty = true)
    @FeatureMemberInjectors
    val contributedMemberInjectors: Map<KClass<*>, MembersInjector<*>>

    /**
     * The ten objectives, in order. `ObjectivesPlugin` takes this list and is built here, so the
     * objectives are contributed to this graph too.
     */
    @Provides
    fun objectivesList(objectives: Map<Int, Objective>): List<Objective> =
        objectives.toList().sortedBy { it.first }.map { it.second }

    /**
     * Constraint plugins that are also bound to an interface, or injected directly.
     * to these instances rather than building their own.
     */
    /**
     * Objects the graph exposes for code that resolves them by hand rather than by injection.
     */
    val trendCalculator: TrendCalculator
    val resourceHelper: ResourceHelper
    val fabricPrivacy: FabricPrivacy
    val uiInteraction: UiInteraction
    val carbSuggestionActions: CarbSuggestionActions
    val temporaryBasalStorage: TemporaryBasalStorage
    val detailedBolusInfoStorage: DetailedBolusInfoStorage
    val blePreCheck: BlePreCheck
    val visibilityContext: VisibilityContext
    val cloudDirectoryManager: CloudDirectoryManager
    val graphConfigRepository: GraphConfigRepository
    val batchExecutor: BatchExecutor
    val wizardBolusExecutor: WizardBolusExecutor
    val loggerUtils: LoggerUtils
    val importExportPrefs: ImportExportPrefs
    val preferences: Preferences
    val pumpWithConcentration: PumpWithConcentration
    val calculationWorkflow: CalculationWorkflow
    val workflowChainData: WorkflowChainData
    val aapsLogger: AAPSLogger
    val rxBus: RxBus
    val dateUtil: DateUtil
    val l: L
    val aapsSchedulers: AapsSchedulers
    val sp: SP
    val widgetUpdater: WidgetUpdater
    val sceneIconResolver: SceneIconResolver
    val processedDeviceStatusData: ProcessedDeviceStatusData
    val lastLocationDataContainer: LastLocationDataContainer
    val storeDataForDb: StoreDataForDb
    val sceneExecutor: SceneExecutor
    val dataInbox: DataInbox
    val autosensData: AutosensData
    val commandQueue: CommandQueue
    val localAlertUtils: LocalAlertUtils
    val bolusProgressData: BolusProgressData
    val persistenceLayer: PersistenceLayer
    val cloudStorageManager: CloudStorageManager
    val overviewDataCache: OverviewDataCache
    val calculationSignals: CalculationSignals
    val calculationSignalsEmitter: CalculationSignalsEmitter
    val appRepository: AppRepository
    val cloudStorageProviders: Set<CloudStorageProvider>
    val constraintsChecker: ConstraintsChecker
    val nsClientRepository: NSClientRepository
    val builtInSearchables: BuiltInSearchables

    /** Read by `MainApp`. */
    val activityMonitor: ActivityMonitor

    /** The one application scope. */
    @ApplicationScope val appScope: CoroutineScope
    val notificationManager: NotificationManager
    val apsResult: APSResult
    val pumpEnactResult: PumpEnactResult
    val profileSwitchSilentGate: ProfileSwitchSilentGate
    val profileSwitchExpiryScheduler: ProfileSwitchExpiryScheduler
    val snackbarNotificationFallback: SnackbarNotificationFallback
    val runningConfiguration: RunningConfiguration
    val runningConfigurationKeys: RunningConfigurationKeys
    val activePlugin: ActivePlugin
    val pluginPermissions: PluginPermissions
    val pluginStore: PluginStore
    val xDripBroadcast: XDripBroadcast
    val maintenance: Maintenance
    val fileListProvider: FileListProvider
    val lastBgData: LastBgData
    val localeDependentSetting: LocaleDependentSetting
    val pumpStatusProvider: PumpStatusProvider
    val passwordCheck: PasswordCheck
    val overviewData: OverviewData
    val sharedPreferences: SharedPreferences
    val exportPasswordDataStore: ExportPasswordDataStore
    val secureEncrypt: SecureEncrypt
    val cryptoUtil: CryptoUtil
    val concentrationHelper: ConcentrationHelper
    val processedTbrEbData: ProcessedTbrEbData
    val userEntryLogger: UserEntryLogger
    val glucoseStatusProvider: GlucoseStatusProvider
    val notificationHolder: NotificationHolder
    val userEntryPresentationHelper: UserEntryPresentationHelper
    val profiler: Profiler
    val alarmSoundPlayer: AlarmSoundPlayer
    val wizardExecutor: WizardExecutor
    val configBuilder: ConfigBuilder

    // Read by MainApp, which builds this graph and then starts these itself. The two `*Impl` types are
    // here rather than their interfaces because only the concrete class has `start()`; both are
    // `@SingleIn`, so it is the same object the interface binding hands out.
    val config: Config
    val resourceHelperImpl: ResourceHelperImpl
    val fabricPrivacyImpl: FabricPrivacyImpl
    val processLifecycleListener: ProcessLifecycleListener
    val dataSyncSelectorXdrip: DataSyncSelectorXdrip
    val activeSceneSync: ActiveSceneSync

    /**
     * The same object as [activeSceneSync], by class.
     *
     * `SceneExecutor`, `SceneAutomationApiImpl` and `SceneExpiryRunner` all ask for the concrete type and
     * would otherwise get a copy of their own - and an unscoped one, since the
     * class carries only Metro's `@SingleIn`. The scene then activated on an object no screen was reading.
     */
    val activeSceneManager: ActiveSceneManager
    val sceneChainResolver: SceneChainResolver
    val sceneStore: SceneStore
    val scenes: Scenes
    val sceneActions: SceneActions
    val sceneAutomationApi: SceneAutomationApi
    val decimalFormatter: DecimalFormatter
    val profileUtil: ProfileUtil
    val hardLimits: HardLimits
    val nsIncomingDataProcessor: NsIncomingDataProcessor
    val openHumansMetroBridge: OpenHumansMetroBridge
    val xdripMvvmRepository: XdripMvvmRepository
    val wearPlugin: WearPlugin
    val rateLimit: RateLimit
    val tidepoolRepository: TidepoolRepository
    val tidepoolUploader: TidepoolUploader
    val authFlowOut: AuthFlowOut
    val smsCommunicatorRepository: SmsCommunicatorRepository
    val smsCommunicatorPlugin: SmsCommunicatorPlugin
    val pairingOfferPublisher: PairingOfferPublisher
    val pairingOfferFetcher: PairingOfferFetcher
    val clientPairingRepository: ClientPairingRepository
    val clientControlPublisher: ClientControlPublisher
    val authorizedClientsRepository: AuthorizedClientsRepository
    val receiverDelegate: ReceiverDelegate
    val nsClientV3Plugin: NSClientV3Plugin
    val workManager: WorkManager
    val clientControlActionDispatcher: ClientControlActionDispatcher
    val nsClient: NsClient
    val profileFunction: ProfileFunction
    val versionCheckerUtils: VersionCheckerUtils
    val storage: Storage
    val receiverStatusStore: ReceiverStatusStore

    val translator: Translator
    val protectionCheck: ProtectionCheck
    val tddCalculator: TddCalculator
    val tirCalculator: TirCalculator
    val dexcomTirCalculator: DexcomTirCalculator
    val pumpSync: PumpSync
    val iconsProvider: IconsProvider
    val insulinManager: InsulinManager
    val profileRepository: ProfileRepository
    val profileStore: ProfileStore

    val bgQualityCheckPlugin: BgQualityCheckPlugin
    val dstHelperPlugin: DstHelperPlugin
    val dstHelper: DstHelper
    val objectivesPlugin: ObjectivesPlugin

    /**
     * Automation, and the permission providers it is the only contributor to.
     *
     * `AutomationRuntime` lives here.
     * instead of the other way round.
     */
    val automation: Automation

    /** Same object as [automation], by class: ComposeMainActivity and AppNavGraph inject the concrete type. */
    val automationRuntime: AutomationRuntime
    val permissionProviders: Set<PermissionProvider>

    /** The live loop's calculator. A history window has its own, at `HistoryWindowScope`. */
    val iobCobCalculator: IobCobCalculator

    /** The loop. */
    val loop: Loop

    /** Autotune, for the automation actions. */
    val autotune: Autotune

    /** Running-mode helpers, from commonMain. `MainApp` reads the reconciler. */
    val runningModeReconciler: RunningModeReconciler
    val runningModeExpiryJob: RunningModeExpiryJob

    /**
     * The openAPS pieces.
     *
     * Only the four the instrumented APS tests inject. `DeltaCalculator` and the AutoISF glucose status
     * calculator are reached through these, so nothing outside Metro ever asks for them by name.
     */
    val glucoseStatusCalculatorSMB: GlucoseStatusCalculatorSMB
    val determineBasalSMB: DetermineBasalSMB
    val determineBasalAMA: DetermineBasalAMA
    val determineBasalAutoISF: DetermineBasalAutoISF
    val glucoseStatusCalculatorAutoIsf: GlucoseStatusCalculatorAutoIsf
    val deltaCalculator: DeltaCalculator
    val signatureVerifierPlugin: SignatureVerifierPlugin

    /**
     * Source plugins that are also bound to an interface for other callers.
     *
     * Built here, so nothing constructs a second copy.
     */
    val xdripSourcePlugin: XdripSourcePlugin
    val nsClientSourcePlugin: NSClientSourcePlugin
    val nsClientSource: NSClientSource
    val dexcomPlugin: DexcomPlugin

    val sourceGraph: SourceMetroGraph
    /**
     * Interfaces backed by a plugin this graph already builds. A `@Provides` rather than a delegate,
     * because the instance is the plugin.
 *
 * This used to claim that binding it any other way "would make a second one". That is not true of
 * Metro: `@ContributesBinding` on a `@SingleIn` class resolves to the same scoped instance, which is
 * why `DexcomPlugin` and `XdripSourcePlugin` carry their second binding on the class now. Only
 * `BgQualityCheckPlugin` still needs stating here - see below.
     */
    /** Metro already builds the plugin; the openAPS plugins ask for the interface. */
    @Provides fun bgQualityCheck(plugin: BgQualityCheckPlugin): BgQualityCheck = plugin

    @DependencyGraph.Factory
    fun interface Factory {

        /**
         * The graph's inputs - what the **caller** genuinely decides.
         *
         * The last two are how an instrumented test differs from production, and they are parameters
         * rather than replaced bindings for a reason worth keeping: contributions declared in
         * `androidTest` never reach this graph, because it is compiled during
         * `:app:compileFullDebugKotlin` and androidTest is a later, separate compilation. Metro's
         * `replaces` is real, but it cannot help from there. The factory is the only way in.
         */
        fun create(
            /**
             * The application scope, passed in rather than built here so the caller decides its
             * dispatcher: production uses `Dispatchers.Default`, the unit tests an Unconfined one, so
             * that work started while the graph is being built runs on the calling thread.
             */
            @Provides @ApplicationScope appScope: CoroutineScope,

            /** The application context. Android owns it, so it is passed in rather than bound. */
            @Provides context: Context,

            /**
             * The Application, handed back to the graph as the injector for anything Android builds
             * itself - a service or a receiver filling its own fields.
             */
            @Provides memberInjector: MetroMemberInjector,

            /** Which database to open. `DatabaseConfig.IN_MEMORY` is what the instrumented tests pass. */
            @Provides databaseConfig: DatabaseConfig,

            /** Extra external options to report enabled. `ExternalOptionsOverride.NONE` in production. */
            @Provides externalOptionsOverride: ExternalOptionsOverride,

            @Includes coreObjects: CoreObjectsGraph,
            @Includes aiProfileBindings: AiProfileBindings
        ): AppRootGraph
    }
}

