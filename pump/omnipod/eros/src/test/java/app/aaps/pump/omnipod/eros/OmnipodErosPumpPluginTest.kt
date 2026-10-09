package app.gmaaps.pump.omnipod.eros

import app.gmaaps.core.interfaces.protection.ProtectionCheck
import app.gmaaps.core.interfaces.pump.BlePreCheck
import app.gmaaps.core.interfaces.pump.PumpSync
import app.gmaaps.core.interfaces.queue.CommandQueue
import app.gmaaps.core.interfaces.ui.UiInteraction
import app.gmaaps.pump.common.hw.rileylink.RileyLinkUtil
import app.gmaaps.pump.common.hw.rileylink.service.RileyLinkServiceData
import app.gmaaps.pump.omnipod.eros.driver.manager.ErosPodStateManager
import app.gmaaps.pump.omnipod.eros.history.database.ErosHistoryDatabase
import app.gmaaps.pump.omnipod.eros.manager.AapsOmnipodErosManager
import app.gmaaps.pump.omnipod.eros.util.AapsOmnipodUtil
import app.gmaaps.pump.omnipod.eros.util.OmnipodAlertUtil
import app.gmaaps.shared.tests.TestBaseWithProfile
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTimeZone
import org.joda.time.tz.UTCProvider
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.kotlin.whenever

class OmnipodErosPumpPluginTest : TestBaseWithProfile() {

    @Mock lateinit var aapsOmnipodErosManager: AapsOmnipodErosManager
    @Mock lateinit var uiInteraction: UiInteraction
    @Mock lateinit var commandQueue: CommandQueue
    @Mock lateinit var rileyLinkUtil: RileyLinkUtil
    @Mock lateinit var pumpSync: PumpSync
    @Mock lateinit var erosHistoryDatabase: ErosHistoryDatabase
    @Mock lateinit var erosPodStateManager: ErosPodStateManager
    @Mock lateinit var rileyLinkServiceData: RileyLinkServiceData
    @Mock lateinit var aapsOmnipodUtil: AapsOmnipodUtil
    @Mock lateinit var omnipodAlertUtil: OmnipodAlertUtil
    @Mock lateinit var protectionCheck: ProtectionCheck
    @Mock lateinit var blePreCheck: BlePreCheck

    private lateinit var plugin: OmnipodErosPumpPlugin

    @BeforeEach
    fun prepare() {
        DateTimeZone.setProvider(UTCProvider())
        whenever(rh.gs(ArgumentMatchers.anyInt(), ArgumentMatchers.anyLong()))
            .thenReturn("")

        plugin = OmnipodErosPumpPlugin(
            aapsLogger, rh, preferences, commandQueue, rxBus, context,
            erosPodStateManager, aapsOmnipodErosManager, fabricPrivacy, rileyLinkServiceData, aapsOmnipodUtil,
            rileyLinkUtil, omnipodAlertUtil, pumpSync, uiInteraction, notificationManager, erosHistoryDatabase, pumpEnactResultProvider,
            protectionCheck, blePreCheck
        )
    }

    @Test fun `setTempBasalPercent should throw because pump does not support percent basal rate`() {
        assertThrows<IllegalStateException> {
            runBlocking { plugin.setTempBasalPercent(80, 30, false, PumpSync.TemporaryBasalType.NORMAL) }
        }
    }
}

