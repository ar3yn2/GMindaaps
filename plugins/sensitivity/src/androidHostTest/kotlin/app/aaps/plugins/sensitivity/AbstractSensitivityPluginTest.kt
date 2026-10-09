package app.gmaaps.plugins.sensitivity

import androidx.collection.LongSparseArray
import app.gmaaps.core.data.model.PS
import app.gmaaps.core.data.model.TE
import app.gmaaps.core.interfaces.aps.AutosensData
import app.gmaaps.core.interfaces.aps.AutosensDataStore
import app.gmaaps.core.interfaces.aps.AutosensResult
import app.gmaaps.core.interfaces.aps.Sensitivity
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.plugin.PluginDescription
import app.gmaaps.core.interfaces.profile.EffectiveProfile
import app.gmaaps.core.interfaces.resources.ResourceHelper
import app.gmaaps.core.keys.DoubleKey
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.shared.tests.TestBase
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class AbstractSensitivityPluginTest : TestBase() {

    @Mock lateinit var pluginDescription: PluginDescription
    @Mock lateinit var rh: ResourceHelper
    @Mock lateinit var preferences: Preferences

    private inner class SensitivityTestClass(pluginDescription: PluginDescription, aapsLogger: AAPSLogger, rh: ResourceHelper) :
        AbstractSensitivityPlugin(pluginDescription, aapsLogger, rh, preferences, mock()) {

        override fun detectSensitivity(
            ads: AutosensDataStore,
            fromTime: Long,
            toTime: Long,
            profile: EffectiveProfile?,
            siteChanges: List<TE>,
            profileSwitches: List<PS>
        ): AutosensResult = AutosensResult()

        override val id: Sensitivity.SensitivityType
            get() = Sensitivity.SensitivityType.UNKNOWN

        override fun maxAbsorptionHours(): Double = 8.0
        override val isMinCarbsAbsorptionDynamic: Boolean = true
        override val isOref1: Boolean = true

        fun firstIndexAtOrAfterForTest(table: LongSparseArray<AutosensData>, time: Long) = firstIndexAtOrAfter(table, time)
    }

    @Test
    fun fillResultTest() {
        val sut = SensitivityTestClass(pluginDescription, aapsLogger, rh)
        // 12 values -> autosens contribution 0 -> ratio forced to 1.0, even though raw 1.5 would clamp to 1.2
        var ar = sut.fillResult(1.5, 1.0, "1", "1.2", "1", 12, 0.7, 1.2)
        assertThat(ar.ratio).isWithin(0.01).of(1.0)
        ar = sut.fillResult(1.2, 1.0, "1", "1.2", "1", 40, 0.7, 1.2)
        assertThat(ar.ratio).isWithin(0.01).of(1.16)
        ar = sut.fillResult(1.2, 1.0, "1", "1.2", "1", 50, 0.7, 1.2)
        assertThat(ar.ratio).isWithin(0.01).of(1.2)
        ar = sut.fillResult(1.2, 1.0, "1", "1.2", "1", 50, 0.7, 1.1)
        assertThat(ar.ratio).isWithin(0.01).of(1.1)
    }

    @Test
    fun fillResultUsesPreferenceLimits() {
        whenever(preferences.get(DoubleKey.AutosensMin)).thenReturn(0.7)
        whenever(preferences.get(DoubleKey.AutosensMax)).thenReturn(1.2)
        val sut = SensitivityTestClass(pluginDescription, aapsLogger, rh)
        // 48 values -> full autosens; raw ratio 1.5 clamped to the AutosensMax preference (1.2)
        val ar = sut.fillResult(1.5, 10.0, "past", "", "result", 48)
        assertThat(ar.ratio).isWithin(0.01).of(1.2)
        assertThat(ar.carbsAbsorbed).isWithin(0.01).of(10.0)
        assertThat(ar.pastSensitivity).isEqualTo("past")
        assertThat(ar.sensResult).isEqualTo("result")
        assertThat(ar.ratioLimit).contains("Ratio limited")
    }

    @Test
    fun fillResultAddsPartialDataMessage() {
        val sut = SensitivityTestClass(pluginDescription, aapsLogger, rh)
        // 24 of 48 values -> autosens contribution 1/3 -> 0.333 * (1.2 - 1) + 1 = 1.07
        val ar = sut.fillResult(1.2, 1.0, "1", "", "1", 24, 0.7, 1.2)
        assertThat(ar.ratio).isWithin(0.01).of(1.07)
        assertThat(ar.ratioLimit).contains("24 of")
    }

    @Test
    fun firstIndexAtOrAfterTest() {
        val sut = SensitivityTestClass(pluginDescription, aapsLogger, rh)
        val value = mock<AutosensData>()
        val table = LongSparseArray<AutosensData>()
        listOf(100L, 200L, 300L).forEach { table.put(it, value) }
        // exact key -> that index
        assertThat(sut.firstIndexAtOrAfterForTest(table, 200L)).isEqualTo(1)
        // between keys -> first index strictly after
        assertThat(sut.firstIndexAtOrAfterForTest(table, 150L)).isEqualTo(1)
        // before all keys -> 0 (scan the whole table)
        assertThat(sut.firstIndexAtOrAfterForTest(table, 50L)).isEqualTo(0)
        // after all keys -> size (nothing in window)
        assertThat(sut.firstIndexAtOrAfterForTest(table, 400L)).isEqualTo(3)
        // empty table -> 0
        assertThat(sut.firstIndexAtOrAfterForTest(LongSparseArray<AutosensData>(), 100L)).isEqualTo(0)
    }
}

