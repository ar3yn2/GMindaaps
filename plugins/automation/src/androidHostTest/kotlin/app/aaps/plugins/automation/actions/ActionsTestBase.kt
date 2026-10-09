package app.gmaaps.plugins.automation.actions

import app.gmaaps.core.data.model.GlucoseUnit
import app.gmaaps.core.interfaces.alerts.ReminderScheduler
import app.gmaaps.core.interfaces.aps.Loop
import app.gmaaps.core.interfaces.autotune.Autotune
import app.gmaaps.core.interfaces.configuration.ConfigBuilder
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.iob.GlucoseStatusProvider
import app.gmaaps.core.interfaces.maintenance.ImportExportPrefs
import app.gmaaps.core.interfaces.protection.ExportPasswordDataStore
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.scenes.SceneAutomationApi
import app.gmaaps.core.interfaces.scenes.SceneIconResolver
import app.gmaaps.core.interfaces.smsCommunicator.SmsCommunicator
import app.gmaaps.implementation.pump.PumpEnactResultObject
import app.gmaaps.plugins.automation.AutomationStringsValues
import app.gmaaps.shared.tests.TestBaseWithProfile
import app.gmaaps.shared.tests.generatedTextResolver
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

open class
ActionsTestBase : TestBaseWithProfile() {

    @Mock lateinit var smsCommunicator: SmsCommunicator
    @Mock lateinit var loop: Loop
    @Mock lateinit var persistenceLayer: PersistenceLayer
    @Mock lateinit var sceneApi: SceneAutomationApi
    @Mock lateinit var sceneIconResolver: SceneIconResolver
    @Mock lateinit var glucoseStatusProvider: GlucoseStatusProvider
    @Mock lateinit var autotunePlugin: Autotune
    @Mock lateinit var importExportPrefs: ImportExportPrefs
    @Mock lateinit var exportPasswordDataStore: ExportPasswordDataStore
    @Mock lateinit var configBuilder: ConfigBuilder

    /**
     * Real English for every name and description an action renders, so the expected text is what the
     * user reads. `:shared:tests` cannot see this module, so the generated map is handed over here.
     */
    val text: TextResolver = generatedTextResolver("automation" to AutomationStringsValues::textOf)

    /**
     * Actions take their dependencies through the constructor now, so most tests build the action
     * they exercise directly. This is here for the few that go through the factory, i.e. the ones
     * testing [ActionFactory.instantiate].
     */
    val triggerDeps: app.gmaaps.plugins.automation.triggers.TriggerDeps by lazy {
        app.gmaaps.plugins.automation.triggers.TriggerDeps(
            aapsLogger, rxBus, text, profileFunction, profileUtil, preferences, mock(), persistenceLayer,
            activePlugin, mock(), glucoseStatusProvider, dateUtil
        )
    }

    val actionFactory: ActionFactory by lazy {
        ActionFactory(
            triggerDeps, aapsLogger, text, { pumpEnactResultProvider() }, rxBus, dateUtil, mock<ReminderScheduler>(),
            config, persistenceLayer, profileFunction, profileRepository, profileUtil, glucoseStatusProvider,
            notificationManager, activePlugin, preferences, sceneApi, sceneIconResolver, smsCommunicator,
            autotunePlugin, importExportPrefs, exportPasswordDataStore, configBuilder
        )
    }

    @BeforeEach
    fun prepareActionMocks() {
        // An action reports its outcome through PumpEnactResult, which resolves the comment itself. The
        // base builds that with `baseText`, which knows only the five owners :shared:tests can see - an
        // automation string would not resolve. Same resolver as the actions, so the comment resolves too.
        pumpEnactResultProvider = { PumpEnactResultObject(text) }
        whenever(profileFunction.getUnits()).thenReturn(GlucoseUnit.MGDL)
        runBlocking {
            whenever(profileFunction.getProfile()).thenReturn(effectiveProfile)
            whenever(loop.handleRunningModeChange(anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyInt(), anyOrNull())).thenReturn(true)
        }

    }
}

