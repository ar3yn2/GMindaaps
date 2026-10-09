package app.gmaaps.plugins.sync.smsCommunicator.compose

import androidx.compose.runtime.Composable
import app.gmaaps.core.ui.compose.ComposablePluginContent
import app.gmaaps.core.ui.compose.ToolbarConfig
import app.gmaaps.core.ui.compose.metroViewModel

class SmsCommunicatorComposeContent : ComposablePluginContent {

    @Composable
    override fun Render(
        setToolbarConfig: (ToolbarConfig) -> Unit,
        onNavigateBack: () -> Unit,
        onSettings: (() -> Unit)?
    ) {
        val viewModel: SmsCommunicatorViewModel = metroViewModel()

        SmsCommunicatorScreen(viewModel = viewModel)
    }
}

