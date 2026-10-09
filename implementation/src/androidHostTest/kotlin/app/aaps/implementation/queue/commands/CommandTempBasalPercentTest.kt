package app.gmaaps.implementation.queue.commands

import app.gmaaps.core.interfaces.pump.PumpSync
import app.gmaaps.core.interfaces.pump.PumpWithConcentration
import app.gmaaps.core.interfaces.queue.Command
import app.gmaaps.core.interfaces.queue.cancel
import app.gmaaps.implementation.pump.PumpEnactResultObject
import app.gmaaps.shared.tests.TestBaseWithProfile
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class CommandTempBasalPercentTest : TestBaseWithProfile() {

    private fun newCommand(
        percent: Int = 80,
        durationInMinutes: Int = 30,
        enforceNew: Boolean = true,
        tbrType: PumpSync.TemporaryBasalType = PumpSync.TemporaryBasalType.NORMAL
    ) = CommandTempBasalPercent(
        aapsLogger, rh, activePlugin, pumpEnactResultProvider::invoke,
        percent, durationInMinutes, enforceNew, tbrType
    )

    @Test
    fun `execute with non-100 percent calls setTempBasalPercent`() = runTest {
        val pumpResult = PumpEnactResultObject(rh).success(true).enacted(true)
        val pump = mock<PumpWithConcentration> {
            on { setTempBasalPercent(80, 30, true, PumpSync.TemporaryBasalType.NORMAL) } doReturn pumpResult
        }
        whenever(activePlugin.activePump).thenReturn(pump)

        val result = newCommand(percent = 80).execute()

        assertThat(result).isSameInstanceAs(pumpResult)
        verify(pump, never()).cancelTempBasal(true)
    }

    @Test
    fun `execute with 100 percent cancels TBR instead`() = runTest {
        val pumpResult = PumpEnactResultObject(rh).success(true).enacted(true)
        val pump = mock<PumpWithConcentration> {
            on { cancelTempBasal(true) } doReturn pumpResult
        }
        whenever(activePlugin.activePump).thenReturn(pump)

        val result = newCommand(percent = 100).execute()

        assertThat(result).isSameInstanceAs(pumpResult)
        verify(pump, never()).setTempBasalPercent(any(), any(), any(), any())
    }

    @Test
    fun `executeAndComplete completes with execute result`() = runTest {
        val pumpResult = PumpEnactResultObject(rh).success(true).enacted(true)
        val pump = mock<PumpWithConcentration> {
            on { setTempBasalPercent(80, 30, true, PumpSync.TemporaryBasalType.NORMAL) } doReturn pumpResult
        }
        whenever(activePlugin.activePump).thenReturn(pump)
        val command = newCommand(percent = 80)

        command.executeAndComplete()

        val received = command.completion.await()
        assertThat(received).isSameInstanceAs(pumpResult)
    }

    @Test
    fun `cancel completes with success by default`() = runTest {
        whenever(rh.gs(app.gmaaps.core.ui.R.string.command_replaced)).thenReturn("replaced")
        val command = newCommand()

        command.cancel(app.gmaaps.core.ui.R.string.command_replaced)

        val received = command.completion.await()
        assertThat(received.success).isTrue()
    }

    @Test
    fun `cancel completes with failure when success=false`() = runTest {
        whenever(rh.gs(app.gmaaps.core.ui.R.string.command_replaced)).thenReturn("replaced")
        val command = newCommand()

        command.cancel(app.gmaaps.core.ui.R.string.command_replaced, success = false)

        val received = command.completion.await()
        assertThat(received.success).isFalse()
    }

    @Test
    fun `commandType is TEMPBASAL`() {
        assertThat(newCommand().commandType).isEqualTo(Command.CommandType.TEMPBASAL)
    }

    @Test
    fun `log includes percent and duration`() {
        assertThat(newCommand(percent = 75, durationInMinutes = 45).log()).isEqualTo("TEMP BASAL 75% 45 min")
    }
}

