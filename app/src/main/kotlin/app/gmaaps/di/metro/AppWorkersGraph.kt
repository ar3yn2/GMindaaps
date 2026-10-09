package app.gmaaps.di.metro

import androidx.work.ListenableWorker
import app.gmaaps.core.objects.workflow.MetroWorkerCreator
import app.gmaaps.core.objects.workflow.WorkerKey
import app.gmaaps.implementation.maintenance.ImportExportPrefsImpl
import app.gmaaps.implementation.receivers.KeepAliveWorker
import app.gmaaps.implementation.scenes.SceneExpiryWorker
import app.gmaaps.plugins.aps.loop.runningMode.RunningModeExpiryScheduler
import app.gmaaps.plugins.aps.loop.runningMode.RunningModeExpiryWorker
import app.gmaaps.plugins.sync.nsclientV3.workers.DataSyncWorker
import app.gmaaps.plugins.sync.nsclientV3.workers.LoadBgWorker
import app.gmaaps.plugins.sync.nsclientV3.workers.LoadDeviceStatusWorker
import app.gmaaps.plugins.sync.nsclientV3.workers.LoadFoodsWorker
import app.gmaaps.plugins.sync.nsclientV3.workers.LoadLastModificationWorker
import app.gmaaps.plugins.sync.nsclientV3.workers.LoadProfileStoreWorker
import app.gmaaps.plugins.sync.nsclientV3.workers.LoadSettingsWorker
import app.gmaaps.plugins.sync.nsclientV3.workers.LoadStatusWorker
import app.gmaaps.plugins.sync.nsclientV3.workers.LoadTreatmentsWorker
import app.gmaaps.plugins.sync.smsCommunicator.SmsCommunicatorPlugin
import app.gmaaps.plugins.sync.xdrip.workers.XdripDataSyncWorker
import app.gmaaps.workflow.PostCalculationWorker
import app.gmaaps.workflow.PrepareGraphDataWorker
import dev.zacsweers.metro.GraphExtension
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.Provides
import kotlin.reflect.KClass

/**
 * This graph exists to answer one question: can Metro build an Android entry point that the framework
 * constructs for us? A worker is the hardest of the four, because WorkManager holds the constructor
 * call and only hands over a class name at runtime. The answer is the map below - a real compile-time
 * `@IntoMap` multibinding keyed by worker class, which [MetroWorkerFactory] looks up.
 * This is a graph extension of [AppRootGraph], so it declares only what it adds: the app-wide
 * dependencies live once in the root and are inherited, and its factory takes no arguments.
 */
@GraphExtension
interface AppWorkersGraph {

    val runningModeExpiryScheduler: RunningModeExpiryScheduler

    /** Every Metro-wired worker, keyed by its class. [MetroWorkerFactory] matches on the class name. */
    val workerCreators: Map<KClass<out ListenableWorker>, MetroWorkerCreator>

    @Provides
    @IntoMap
    @WorkerKey(RunningModeExpiryWorker::class)
    fun bindRunningModeExpiryWorker(factory: RunningModeExpiryWorker.Factory): MetroWorkerCreator = factory

    @Provides
    @IntoMap
    @WorkerKey(KeepAliveWorker::class)
    fun bindKeepAliveWorker(factory: KeepAliveWorker.Factory): MetroWorkerCreator = factory

    @Provides
    @IntoMap
    @WorkerKey(SceneExpiryWorker::class)
    fun bindSceneExpiryWorker(factory: SceneExpiryWorker.Factory): MetroWorkerCreator = factory

    @Provides
    @IntoMap
    @WorkerKey(ImportExportPrefsImpl.CsvExportWorker::class)
    fun bindCsvExportWorker(f: ImportExportPrefsImpl.CsvExportWorker.Factory): MetroWorkerCreator = f

    @Provides
    @IntoMap
    @WorkerKey(ImportExportPrefsImpl.ApsResultExportWorker::class)
    fun bindApsResultExportWorker(
        f: ImportExportPrefsImpl.ApsResultExportWorker.Factory
    ): MetroWorkerCreator = f

    @Provides
    @IntoMap
    @WorkerKey(DataSyncWorker::class)
    fun bindDataSyncWorker(f: DataSyncWorker.Factory): MetroWorkerCreator = f

    @Provides
    @IntoMap
    @WorkerKey(LoadBgWorker::class)
    fun bindLoadBgWorker(f: LoadBgWorker.Factory): MetroWorkerCreator = f

    @Provides
    @IntoMap
    @WorkerKey(LoadDeviceStatusWorker::class)
    fun bindLoadDeviceStatusWorker(f: LoadDeviceStatusWorker.Factory): MetroWorkerCreator = f

    @Provides
    @IntoMap
    @WorkerKey(LoadFoodsWorker::class)
    fun bindLoadFoodsWorker(f: LoadFoodsWorker.Factory): MetroWorkerCreator = f

    @Provides
    @IntoMap
    @WorkerKey(LoadLastModificationWorker::class)
    fun bindLoadLastModificationWorker(f: LoadLastModificationWorker.Factory): MetroWorkerCreator = f

    @Provides
    @IntoMap
    @WorkerKey(LoadProfileStoreWorker::class)
    fun bindLoadProfileStoreWorker(f: LoadProfileStoreWorker.Factory): MetroWorkerCreator = f

    @Provides
    @IntoMap
    @WorkerKey(LoadSettingsWorker::class)
    fun bindLoadSettingsWorker(f: LoadSettingsWorker.Factory): MetroWorkerCreator = f

    @Provides
    @IntoMap
    @WorkerKey(LoadStatusWorker::class)
    fun bindLoadStatusWorker(f: LoadStatusWorker.Factory): MetroWorkerCreator = f

    @Provides
    @IntoMap
    @WorkerKey(LoadTreatmentsWorker::class)
    fun bindLoadTreatmentsWorker(f: LoadTreatmentsWorker.Factory): MetroWorkerCreator = f

    @Provides
    @IntoMap
    @WorkerKey(XdripDataSyncWorker::class)
    fun bindXdripDataSyncWorker(f: XdripDataSyncWorker.Factory): MetroWorkerCreator = f

    @Provides
    @IntoMap
    @WorkerKey(PostCalculationWorker::class)
    fun bindPostCalculationWorker(f: PostCalculationWorker.Factory): MetroWorkerCreator = f

    @Provides
    @IntoMap
    @WorkerKey(PrepareGraphDataWorker::class)
    fun bindPrepareGraphDataWorker(f: PrepareGraphDataWorker.Factory): MetroWorkerCreator = f

    @Provides
    @IntoMap
    @WorkerKey(SmsCommunicatorPlugin.SmsCommunicatorWorker::class)
    fun bindSmsCommunicatorWorker(f: SmsCommunicatorPlugin.SmsCommunicatorWorker.Factory): MetroWorkerCreator = f
}

