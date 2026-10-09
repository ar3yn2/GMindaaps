package app.gmaaps.plugins.source

import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.resources.ResourceHelper
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.ui.compose.preference.PreferenceSubScreenDef
import app.gmaaps.shared.tests.TestBase
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.kotlin.mock

class TomatoPluginTest : TestBase() {

    private lateinit var tomatoPlugin: TomatoPlugin

    @Mock lateinit var rh: ResourceHelper
    @Mock lateinit var preferences: Preferences
    @Mock lateinit var config: Config

    @BeforeEach
    fun setup() {
        tomatoPlugin = TomatoPlugin(rh, aapsLogger, preferences, config, mock())
    }

    @Test
    fun `plugin is created`() {
        assertThat(tomatoPlugin).isNotNull()
    }

    @Test
    fun `preference screen content is provided`() {
        // exercises AbstractBgSourcePlugin.getPreferenceScreenContent
        assertThat(tomatoPlugin.getPreferenceScreenContent()).isInstanceOf(PreferenceSubScreenDef::class.java)
    }
}

