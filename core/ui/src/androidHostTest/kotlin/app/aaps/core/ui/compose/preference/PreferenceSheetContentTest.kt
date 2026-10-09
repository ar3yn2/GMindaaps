package app.gmaaps.core.ui.compose.preference

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onRoot
import app.gmaaps.core.interfaces.profile.ProfileUtil
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.ui.compose.LocalConfig
import app.gmaaps.core.ui.compose.LocalPreferences
import app.gmaaps.core.ui.compose.LocalProfileUtil
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import app.gmaaps.core.interfaces.configuration.Config as AppConfig
import app.gmaaps.core.ui.R as CoreUiR

/** Robolectric render test for [PreferenceSheetContent]: renders a preference sub-screen definition. */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35])
class PreferenceSheetContentTest {

    @get:Rule
    val compose = createComposeRule()

    private val prefs: Preferences = mock()
    private val config: AppConfig = mock()
    private val profileUtil: ProfileUtil = mock()

    private fun render(block: @Composable () -> Unit) = compose.setContent {
        CompositionLocalProvider(
            LocalPreferences provides prefs,
            LocalConfig provides config,
            LocalProfileUtil provides profileUtil
        ) {
            PreviewTheme { block() }
        }
    }

    @Test
    fun rendersSubScreen() {
        render {
            PreferenceSheetContent(
                settingsDef = PreferenceSubScreenDef(key = "sheet", titleResId = CoreUiR.string.treatments)
            )
        }
        compose.onRoot().assertIsDisplayed()
    }
}

