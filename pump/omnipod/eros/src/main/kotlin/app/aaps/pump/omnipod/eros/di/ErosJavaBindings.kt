package app.gmaaps.pump.omnipod.eros.di

import app.gmaaps.core.interfaces.insulin.ConcentrationHelper
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.notifications.NotificationManager
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.pump.BolusProgressData
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.pump.PumpSync
import app.gmaaps.core.interfaces.resources.ResourceHelper
import app.gmaaps.core.interfaces.rx.AapsSchedulers
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.interfaces.ui.UiInteraction
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.pump.common.hw.rileylink.RileyLinkUtil
import app.gmaaps.pump.common.hw.rileylink.ble.RFSpy
import app.gmaaps.pump.common.hw.rileylink.ble.data.RadioResponse
import app.gmaaps.pump.common.hw.rileylink.service.RileyLinkServiceData
import app.gmaaps.pump.common.hw.rileylink.service.tasks.ServiceTaskExecutor
import app.gmaaps.pump.common.hw.rileylink.service.tasks.WakeAndTuneTask
import app.gmaaps.pump.omnipod.eros.driver.manager.ErosPodStateManager
import app.gmaaps.pump.omnipod.eros.driver.manager.OmnipodManager
import app.gmaaps.pump.omnipod.eros.history.ErosHistory
import app.gmaaps.pump.omnipod.eros.manager.AapsOmnipodErosManager
import app.gmaaps.pump.omnipod.eros.rileylink.manager.OmnipodRileyLinkCommunicationManager
import app.gmaaps.pump.omnipod.eros.util.AapsOmnipodUtil
import app.gmaaps.pump.omnipod.eros.util.OmnipodAlertUtil
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

/**
 * The eros classes that are still **Java**, constructed by hand.
 *
 * Metro generates a factory from an `@Inject` constructor by reading Kotlin IR, which a Java class does
 * not have. A `@Provides` needs none of that - it just calls the constructor - so writing the call out
 * is all it takes to let Metro own a Java class.
 */
@ContributesTo(AppScope::class)
@BindingContainer
object ErosJavaBindings {

    @Provides
    @SingleIn(AppScope::class)
    fun provideOmnipodRileyLinkCommunicationManager(
        aapsLogger: AAPSLogger,
        preferences: Preferences,
        rileyLinkServiceData: RileyLinkServiceData,
        serviceTaskExecutor: ServiceTaskExecutor,
        rfSpy: RFSpy,
        activePlugin: ActivePlugin,
        rileyLinkUtil: RileyLinkUtil,
        wakeAndTuneTask: () -> WakeAndTuneTask,
        radioResponse: () -> RadioResponse
    ): OmnipodRileyLinkCommunicationManager =
        OmnipodRileyLinkCommunicationManager(
            aapsLogger, preferences, rileyLinkServiceData, serviceTaskExecutor,
            rfSpy, activePlugin, rileyLinkUtil, wakeAndTuneTask, radioResponse
        )

    @Provides
    @SingleIn(AppScope::class)
    fun provideAapsOmnipodErosManager(
        delegate: OmnipodManager,
        podStateManager: ErosPodStateManager,
        erosHistory: ErosHistory,
        aapsOmnipodUtil: AapsOmnipodUtil,
        aapsLogger: AAPSLogger,
        rxBus: RxBus,
        preferences: Preferences,
        rh: ResourceHelper,
        omnipodAlertUtil: OmnipodAlertUtil,
        pumpSync: PumpSync,
        uiInteraction: UiInteraction,
        notificationManager: NotificationManager,
        pumpEnactResultProvider: () -> PumpEnactResult,
        ch: ConcentrationHelper,
        bolusProgressData: BolusProgressData
    ): AapsOmnipodErosManager =
        AapsOmnipodErosManager(
            delegate, podStateManager, erosHistory, aapsOmnipodUtil, aapsLogger, rxBus, preferences,
            rh, omnipodAlertUtil, pumpSync, uiInteraction, notificationManager, { pumpEnactResultProvider() },
            ch, bolusProgressData
        )

    /**
     * The pod command delegate. `AapsOmnipodErosManager` used to build this with `new`; it takes it as a
     * parameter now so its command surface can be reached from a test.
     */
    @Provides
    @SingleIn(AppScope::class)
    fun provideOmnipodManager(
        aapsLogger: AAPSLogger,
        aapsSchedulers: AapsSchedulers,
        communicationService: OmnipodRileyLinkCommunicationManager,
        podStateManager: ErosPodStateManager
    ): OmnipodManager = OmnipodManager(aapsLogger, aapsSchedulers, communicationService, podStateManager)
}

