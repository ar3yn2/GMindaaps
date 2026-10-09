package app.gmaaps.plugins.source

import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.resources.ResourceHelper
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.shared.tests.TestBase
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock
import org.mockito.kotlin.mock

class MM640GPluginTest : TestBase() {

    private lateinit var mM640gPlugin: MM640gPlugin

    @Mock lateinit var rh: ResourceHelper
    @Mock lateinit var preferences: Preferences
    @Mock lateinit var config: Config

    @BeforeEach
    fun setup() {
        mM640gPlugin = MM640gPlugin(rh, aapsLogger, preferences, config, mock())
    }

}

