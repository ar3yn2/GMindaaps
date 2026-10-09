package app.gmaaps.plugins.automation

import android.app.Application
import android.content.Intent
import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.notifications.AlarmSound
import app.gmaaps.core.interfaces.resources.ResourceHelper
import app.gmaaps.core.interfaces.ui.UiInteraction
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.shared.tests.TestBase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Covers the receiver that rings a reminder scheduled through
 * [app.gmaaps.core.interfaces.alerts.ReminderScheduler].
 */
class TimerReminderReceiverTest : TestBase() {

    /** A `Context` that is also the injector, which is what `MetroBroadcastReceiver` looks for. */
    abstract class InjectingApplication : Application(), MetroMemberInjector

    @Mock lateinit var uiInteraction: UiInteraction
    @Mock lateinit var rh: ResourceHelper
    @Mock lateinit var config: Config
    @Mock lateinit var context: InjectingApplication

    private lateinit var sut: TimerReminderReceiver

    @BeforeEach
    fun prepare() {
        // MetroBroadcastReceiver injects via context.applicationContext in super.onReceive().
        whenever(context.applicationContext).thenReturn(context)
        whenever(context.injectMembers(any())).thenAnswer { invocation ->
            (invocation.arguments[0] as? TimerReminderReceiver)?.let {
                it.uiInteraction = uiInteraction
                it.rh = rh
                it.config = config
                it.aapsLogger = aapsLogger
            }
            true
        }
        whenever(config.appName).thenReturn(TextRef.Literal("AAPS"))
        whenever(rh.gs(TextRef.Literal("AAPS"))).thenReturn("AAPS")
        sut = TimerReminderReceiver()
    }

    @Test
    fun ringsWithTheScheduledText() {
        val intent = mock<Intent>()
        whenever(intent.getStringExtra(eq(TimerReminderReceiver.EXTRA_TEXT))).thenReturn("Time to eat")

        sut.onReceive(context, intent)

        verify(uiInteraction).runAlarm(status = "Time to eat", title = "AAPS", sound = AlarmSound.ALARM)
    }

    @Test
    fun fallsBackToAppNameWhenTextMissing() {
        val intent = mock<Intent>()
        whenever(intent.getStringExtra(eq(TimerReminderReceiver.EXTRA_TEXT))).thenReturn(null)

        sut.onReceive(context, intent)

        verify(uiInteraction).runAlarm(status = "AAPS", title = "AAPS", sound = AlarmSound.ALARM)
    }

    @Test
    fun fallsBackToAppNameWhenTextIsBlank() {
        // A blank alarm would ring with an empty body, which reads as a bug to the user.
        val intent = mock<Intent>()
        whenever(intent.getStringExtra(eq(TimerReminderReceiver.EXTRA_TEXT))).thenReturn("   ")

        sut.onReceive(context, intent)

        verify(uiInteraction).runAlarm(status = "AAPS", title = "AAPS", sound = AlarmSound.ALARM)
    }
}

