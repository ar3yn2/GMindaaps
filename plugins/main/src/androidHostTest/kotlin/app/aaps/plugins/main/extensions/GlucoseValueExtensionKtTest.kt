package app.gmaaps.plugins.main.extensions

import app.gmaaps.core.data.iob.InMemoryGlucoseValue
import app.gmaaps.core.data.model.GV
import app.gmaaps.core.data.model.GlucoseUnit
import app.gmaaps.core.data.model.SourceSensor
import app.gmaaps.core.data.model.TrendArrow
import app.gmaaps.core.objects.extensions.valueToUnits
import app.gmaaps.core.ui.compose.icons.IcArrowDoubleDown
import app.gmaaps.core.ui.compose.icons.IcArrowDoubleUp
import app.gmaaps.core.ui.compose.icons.IcArrowFlat
import app.gmaaps.core.ui.compose.icons.IcArrowFortyfiveDown
import app.gmaaps.core.ui.compose.icons.IcArrowFortyfiveUp
import app.gmaaps.core.ui.compose.icons.IcArrowInvalid
import app.gmaaps.core.ui.compose.icons.IcArrowSimpleDown
import app.gmaaps.core.ui.compose.icons.IcArrowSimpleUp
import app.gmaaps.core.ui.extensions.directionToIcon
import app.gmaaps.shared.tests.TestBaseWithProfile
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class GlucoseValueExtensionKtTest : TestBaseWithProfile() {

    private val glucoseValue =
        GV(raw = 0.0, noise = 0.0, value = 100.0, timestamp = 1514766900000, sourceSensor = SourceSensor.UNKNOWN, trendArrow = TrendArrow.FLAT)
    private val inMemoryGlucoseValue = InMemoryGlucoseValue(1000, 100.0, sourceSensor = SourceSensor.UNKNOWN)

    @Test
    fun valueToUnitsString() {
    }

    @Test
    fun inMemoryValueToUnits() {
        assertThat(inMemoryGlucoseValue.valueToUnits(GlucoseUnit.MGDL)).isEqualTo(100.0)
        assertThat(inMemoryGlucoseValue.valueToUnits(GlucoseUnit.MMOL)).isWithin(0.01).of(5.55)
    }

    @Test
    fun directionToIcon() {
        assertThat(glucoseValue.trendArrow.directionToIcon()).isEqualTo(IcArrowFlat)
        glucoseValue.trendArrow = TrendArrow.NONE
        assertThat(glucoseValue.trendArrow.directionToIcon()).isEqualTo(IcArrowInvalid)
        glucoseValue.trendArrow = TrendArrow.TRIPLE_DOWN
        assertThat(glucoseValue.trendArrow.directionToIcon()).isEqualTo(IcArrowInvalid)
        glucoseValue.trendArrow = TrendArrow.TRIPLE_UP
        assertThat(glucoseValue.trendArrow.directionToIcon()).isEqualTo(IcArrowInvalid)
        glucoseValue.trendArrow = TrendArrow.DOUBLE_DOWN
        assertThat(glucoseValue.trendArrow.directionToIcon()).isEqualTo(IcArrowDoubleDown)
        glucoseValue.trendArrow = TrendArrow.SINGLE_DOWN
        assertThat(glucoseValue.trendArrow.directionToIcon()).isEqualTo(IcArrowSimpleDown)
        glucoseValue.trendArrow = TrendArrow.FORTY_FIVE_DOWN
        assertThat(glucoseValue.trendArrow.directionToIcon()).isEqualTo(IcArrowFortyfiveDown)
        glucoseValue.trendArrow = TrendArrow.FORTY_FIVE_UP
        assertThat(glucoseValue.trendArrow.directionToIcon()).isEqualTo(IcArrowFortyfiveUp)
        glucoseValue.trendArrow = TrendArrow.SINGLE_UP
        assertThat(glucoseValue.trendArrow.directionToIcon()).isEqualTo(IcArrowSimpleUp)
        glucoseValue.trendArrow = TrendArrow.DOUBLE_UP
        assertThat(glucoseValue.trendArrow.directionToIcon()).isEqualTo(IcArrowDoubleUp)
    }
}

