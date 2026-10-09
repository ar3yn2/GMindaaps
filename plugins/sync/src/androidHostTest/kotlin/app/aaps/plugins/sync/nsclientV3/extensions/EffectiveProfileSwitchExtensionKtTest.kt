package app.gmaaps.plugins.sync.nsclientV3.extensions

import app.gmaaps.core.data.model.EPS
import app.gmaaps.core.data.model.ICfg
import app.gmaaps.core.data.model.IDs
import app.gmaaps.core.data.pump.defs.PumpType
import app.gmaaps.core.nssdk.localmodel.treatment.NSEffectiveProfileSwitch
import app.gmaaps.core.nssdk.mapper.convertToRemoteAndBack
import app.gmaaps.plugins.sync.extensions.contentEqualsTo
import app.gmaaps.shared.tests.TestBaseWithProfile
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class EffectiveProfileSwitchExtensionKtTest : TestBaseWithProfile() {

    private var insulinConfiguration: ICfg = ICfg("Insulin", 360 * 60 * 1000, 60 * 60 * 1000)

    @BeforeEach
    fun mock() {
    }

    @Test
    fun toEffectiveProfileSwitch() {
        val profileSwitch = EPS(
            timestamp = 10000,
            isValid = true,
            basalBlocks = effectiveProfile.basalBlocks,
            isfBlocks = effectiveProfile.isfBlocks,
            icBlocks = effectiveProfile.icBlocks,
            targetBlocks = effectiveProfile.targetBlocks,
            glucoseUnit = effectiveProfile.units,
            originalProfileName = "SomeProfile",
            originalCustomizedName = "SomeProfile (150%, 1h)",
            originalTimeshift = 3600000,
            originalPercentage = 150,
            originalDuration = 3600000,
            originalEnd = 0,
            iCfg = insulinConfiguration.also {
                it.insulinEndTime = (effectiveProfile.iCfg.dia * 3600 * 1000).toLong()
            },
            ids = IDs(
                nightscoutId = "nightscoutId",
                pumpId = 11000,
                pumpType = PumpType.DANA_I,
                pumpSerial = "bbbb"
            )
        )

        val profileSwitch2 = (profileSwitch.toNSEffectiveProfileSwitch(dateUtil).convertToRemoteAndBack() as NSEffectiveProfileSwitch).toEffectiveProfileSwitch(dateUtil, insulinConfiguration)!!
        assertThat(profileSwitch.contentEqualsTo(profileSwitch2)).isTrue()
        assertThat(profileSwitch.ids.contentEqualsTo(profileSwitch2.ids)).isTrue()
    }
}

