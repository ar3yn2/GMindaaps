package app.gmaaps.pump.dana

import app.gmaaps.shared.tests.TestBaseWithProfile
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DanaPumpTest : TestBaseWithProfile() {

    private lateinit var sut: DanaPump

    @BeforeEach
    fun setup() {
        sut = DanaPump(aapsLogger, preferences, dateUtil, decimalFormatter, profileStoreProvider)
    }

    @Test
    fun detectDanaRS() {
        sut.hwModel = 0x05
        assertThat(sut.modelFriendlyName()).contains("DanaRS")
    }
}

