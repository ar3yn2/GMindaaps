package app.gmaaps.plugins.main.extensions

import app.gmaaps.core.data.model.GlucoseUnit
import app.gmaaps.core.data.model.IDs
import app.gmaaps.core.data.model.TT
import app.gmaaps.core.objects.extensions.target
import app.gmaaps.core.ui.extensions.highValueToUnitsToString
import app.gmaaps.core.ui.extensions.lowValueToUnitsToString
import app.gmaaps.shared.tests.TestBaseWithProfile
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TemporaryTargetExtensionKtTest : TestBaseWithProfile() {

    private val temporaryTarget = TT(
        id = 0,
        version = 0,
        dateCreated = -1,
        isValid = true,
        referenceId = null,
        ids = IDs(),
        timestamp = 0,
        utcOffset = 0,
        reason = TT.Reason.AUTOMATION,
        highTarget = 120.0,
        lowTarget = 110.0,
        duration = 1800000
    )

    @Test
    fun lowValueToUnitsToString() {
        assertThat(temporaryTarget.lowValueToUnitsToString(GlucoseUnit.MGDL, decimalFormatter)).isEqualTo("110")
        assertThat(temporaryTarget.lowValueToUnitsToString(GlucoseUnit.MMOL, decimalFormatter)).isEqualTo("6.1")
    }

    @Test
    fun highValueToUnitsToString() {
        assertThat(temporaryTarget.highValueToUnitsToString(GlucoseUnit.MGDL, decimalFormatter)).isEqualTo("120")
        assertThat(temporaryTarget.highValueToUnitsToString(GlucoseUnit.MMOL, decimalFormatter)).isEqualTo("6.7")
    }

    @Test
    fun target() {
        assertThat(temporaryTarget.target()).isEqualTo(115.0)
    }
}

