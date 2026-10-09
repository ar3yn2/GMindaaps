package app.gmaaps.plugins.source

import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.resources.ResourceHelper
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.shared.tests.TestBase
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.kotlin.mock

class PoctechPluginTest : TestBase() {

    private lateinit var poctechPlugin: PoctechPlugin

    @Mock lateinit var rh: ResourceHelper
    @Mock lateinit var preferences: Preferences
    @Mock lateinit var config: Config

    @BeforeEach
    fun setup() {
        poctechPlugin = PoctechPlugin(rh, aapsLogger, preferences, config, mock())
    }

    @Test
    fun `plugin is created`() {
        assertThat(poctechPlugin).isNotNull()
    }
}

