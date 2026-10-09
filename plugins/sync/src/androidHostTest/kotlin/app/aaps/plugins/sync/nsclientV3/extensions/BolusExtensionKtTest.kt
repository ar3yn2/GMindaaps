package app.gmaaps.plugins.sync.nsclientV3.extensions

import app.gmaaps.core.data.model.BS
import app.gmaaps.core.data.model.ICfg
import app.gmaaps.core.data.model.IDs
import app.gmaaps.core.data.pump.defs.PumpType
import app.gmaaps.core.nssdk.localmodel.treatment.NSBolus
import app.gmaaps.core.nssdk.mapper.convertToRemoteAndBack
import app.gmaaps.plugins.sync.extensions.contentEqualsTo
import app.gmaaps.shared.tests.TestBase
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class BolusExtensionKtTest : TestBase() {


    val iCfg = ICfg(insulinLabel = "Fake", insulinEndTime = 9 * 3600 * 1000, insulinPeakTime = 60 * 60 * 1000, concentration = 1.0)

    @Test
    fun toBolus() {
        var bolus = BS(
            timestamp = 10000,
            isValid = true,
            amount = 1.0,
            type = BS.Type.SMB,
            notes = "aaaa",
            isBasalInsulin = false,
            ids = IDs(
                nightscoutId = "nightscoutId",
                pumpId = 11000,
                pumpType = PumpType.DANA_I,
                pumpSerial = "bbbb"
            ),
            iCfg = iCfg
        )

        var bolus2 = (bolus.toNSBolus().convertToRemoteAndBack() as NSBolus).toBolus(iCfg)
        assertThat(bolus.contentEqualsTo(bolus2)).isTrue()
        assertThat(bolus.ids.contentEqualsTo(bolus2.ids)).isTrue()

        bolus = BS(
            timestamp = 10000,
            isValid = false,
            amount = 1.0,
            type = BS.Type.NORMAL,
            notes = "aaaa",
            isBasalInsulin = true,
            ids = IDs(
                nightscoutId = "nightscoutId",
                pumpId = 11000,
                pumpType = PumpType.DANA_I,
                pumpSerial = "bbbb"
            ),
            iCfg = iCfg
        )

        bolus2 = (bolus.toNSBolus().convertToRemoteAndBack() as NSBolus).toBolus(iCfg)
        assertThat(bolus.contentEqualsTo(bolus2)).isTrue()
        assertThat(bolus.ids.contentEqualsTo(bolus2.ids)).isTrue()
    }
}

