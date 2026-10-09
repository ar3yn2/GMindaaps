package app.gmaaps.plugins.sync.nsclientV3.extensions

import app.gmaaps.core.data.model.IDs
import app.gmaaps.core.data.model.TT
import app.gmaaps.core.data.pump.defs.PumpType
import app.gmaaps.core.nssdk.localmodel.treatment.NSTemporaryTarget
import app.gmaaps.core.nssdk.mapper.convertToRemoteAndBack
import app.gmaaps.plugins.sync.extensions.contentEqualsTo
import app.gmaaps.shared.tests.TestBaseWithProfile
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
internal class TemporaryTargetExtensionKtTest : TestBaseWithProfile() {

    @Test
    fun toTemporaryTarget() {
        var temporaryTarget = TT(
            timestamp = 10000,
            isValid = true,
            reason = TT.Reason.ACTIVITY,
            highTarget = 100.0,
            lowTarget = 99.0,
            duration = 3600000,
            ids = IDs(
                nightscoutId = "nightscoutId",
                pumpId = 11000,
                pumpType = PumpType.DANA_I,
                pumpSerial = "bbbb"
            )
        )

        var temporaryTarget2 = (temporaryTarget.toNSTemporaryTarget().convertToRemoteAndBack() as NSTemporaryTarget).toTemporaryTarget()
        assertThat(temporaryTarget.contentEqualsTo(temporaryTarget2)).isTrue()
        assertThat(temporaryTarget.ids.contentEqualsTo(temporaryTarget2.ids)).isTrue()

        temporaryTarget = TT(
            timestamp = 10000,
            isValid = true,
            reason = TT.Reason.CUSTOM,
            highTarget = 150.0,
            lowTarget = 150.0,
            duration = 30000,
            ids = IDs(
                nightscoutId = "nightscoutId",
                pumpId = 11000,
                pumpType = PumpType.DANA_I,
                pumpSerial = "bbbb"
            )
        )

        temporaryTarget2 = (temporaryTarget.toNSTemporaryTarget().convertToRemoteAndBack() as NSTemporaryTarget).toTemporaryTarget()
        assertThat(temporaryTarget.contentEqualsTo(temporaryTarget2)).isTrue()
        assertThat(temporaryTarget.ids.contentEqualsTo(temporaryTarget2.ids)).isTrue()
    }
}

