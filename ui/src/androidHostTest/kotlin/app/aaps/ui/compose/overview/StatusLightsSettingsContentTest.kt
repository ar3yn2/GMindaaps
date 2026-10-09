package app.gmaaps.ui.compose.overview

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.ui.CoreUiStrings
import app.gmaaps.core.ui.compose.LocalConfig
import app.gmaaps.core.ui.compose.LocalPreferences
import app.gmaaps.core.ui.compose.preference.PreferenceSubScreenDef
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import app.gmaaps.core.interfaces.configuration.Config as AppConfig
import app.gmaaps.core.ui.R as CoreUiR

/** Robolectric composable test for [StatusLightsSettingsContent]: renders title + copy action. */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35])
class StatusLightsSettingsContentTest {

    @get:Rule
    val compose = createComposeRule()

    private val preferences: Preferences = mock()
    private val config: AppConfig = mock()

    private lateinit var statusLightsTitle: String

    @Before
    fun setUp() {
        val ctx: Context = RuntimeEnvironment.getApplication()
        statusLightsTitle = ctx.getString(CoreUiR.string.statuslights)
    }

    @Test
    fun rendersTitle() {
        compose.setContent {
            CompositionLocalProvider(LocalPreferences provides preferences, LocalConfig provides config) {
                MaterialTheme {
                    StatusLightsSettingsContent(
                        settingsDef = PreferenceSubScreenDef(key = "statuslights", title = CoreUiStrings.treatments)
                    )
                }
            }
        }
        compose.onNodeWithText(statusLightsTitle).assertIsDisplayed()
    }
}

