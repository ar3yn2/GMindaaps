package app.gmaaps.plugins.source

import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.resources.ResourceHelper
import app.gmaaps.shared.tests.TestBase
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.kotlin.mock

class NSClientSourcePluginTest : TestBase() {

    private lateinit var nsClientSourcePlugin: NSClientSourcePlugin

    @Mock lateinit var rh: ResourceHelper
    @Mock lateinit var config: Config

    @BeforeEach
    fun setup() {
        nsClientSourcePlugin = NSClientSourcePlugin(rh, aapsLogger, config, mock())
    }

    @Test
    fun `plugin is created`() {
        assertThat(nsClientSourcePlugin).isNotNull()
    }
}

