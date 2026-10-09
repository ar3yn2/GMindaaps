package app.gmaaps.plugins.source

import app.gmaaps.shared.tests.TestBaseWithProfile
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.mock

class GlimpPluginTest : TestBaseWithProfile() {

    private lateinit var glimpPlugin: GlimpPlugin

    @BeforeEach
    fun setup() {
        glimpPlugin = GlimpPlugin(rh, aapsLogger, preferences, config, mock())
    }
}

