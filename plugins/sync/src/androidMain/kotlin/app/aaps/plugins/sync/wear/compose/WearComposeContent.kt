package app.gmaaps.plugins.sync.wear.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import app.gmaaps.core.ui.compose.ComposablePluginContent
import app.gmaaps.core.ui.compose.ToolbarConfig
import app.gmaaps.core.ui.compose.metroViewModel

internal class WearComposeContent : ComposablePluginContent {

    @Composable
    override fun Render(
        setToolbarConfig: (ToolbarConfig) -> Unit,
        onNavigateBack: () -> Unit,
        onSettings: (() -> Unit)?
    ) {
        val viewModel: WearViewModel = metroViewModel()

        LaunchedEffect(Unit) {
            viewModel.requestCustomWatchface()
        }

        WearScreen(
            viewModel = viewModel,
            setToolbarConfig = setToolbarConfig,
            onNavigateBack = onNavigateBack,
            onSettings = onSettings
        )
    }
}

