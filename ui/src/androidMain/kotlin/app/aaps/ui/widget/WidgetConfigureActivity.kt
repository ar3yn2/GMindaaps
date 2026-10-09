package app.gmaaps.ui.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.interfaces.ui.SnackbarHostPresence
import app.gmaaps.core.interfaces.widget.WidgetUpdater
import app.gmaaps.core.keys.BooleanComposedKey
import app.gmaaps.core.keys.IntComposedKey
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.ui.compose.AapsTheme
import app.gmaaps.core.ui.compose.LocalPreferences
import app.gmaaps.core.ui.compose.LocalSnackbarHostState
import app.gmaaps.core.ui.compose.MetroAppCompatActivity
import app.gmaaps.core.ui.compose.dialogs.GlobalSnackbarHost
import dev.zacsweers.metro.Inject

class WidgetConfigureActivity : MetroAppCompatActivity() {

    @Inject lateinit var preferences: Preferences
    @Inject lateinit var widgetUpdater: WidgetUpdater
    @Inject lateinit var rxBus: RxBus
    @Inject lateinit var snackbarHostPresence: SnackbarHostPresence

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Default to CANCELED so backing out of the activity cancels widget placement.
        setResult(RESULT_CANCELED)

        appWidgetId = intent.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        val initialOpacity = preferences.get(IntComposedKey.WidgetOpacity, appWidgetId)
        val initialUseBlack = preferences.get(BooleanComposedKey.WidgetUseBlack, appWidgetId)

        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            CompositionLocalProvider(
                LocalPreferences provides preferences,
                LocalSnackbarHostState provides snackbarHostState
            ) {
                AapsTheme {
                    Box(modifier = Modifier.fillMaxSize()) {
                        WidgetConfigureScreen(
                            initialOpacity = initialOpacity,
                            initialUseBlack = initialUseBlack,
                            onOpacityChange = { value ->
                                preferences.put(IntComposedKey.WidgetOpacity, appWidgetId, value = value)
                                widgetUpdater.update("WidgetConfigure")
                            },
                            onUseBlackChange = { value ->
                                preferences.put(BooleanComposedKey.WidgetUseBlack, appWidgetId, value = value)
                                widgetUpdater.update("WidgetConfigure")
                            },
                            onClose = {
                                val resultValue = Intent().apply {
                                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                                }
                                setResult(RESULT_OK, resultValue)
                                finish()
                            }
                        )
                        GlobalSnackbarHost(
                            rxBus = rxBus,
                            snackbarHostPresence = snackbarHostPresence,
                            hostState = snackbarHostState,
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }
    }
}

