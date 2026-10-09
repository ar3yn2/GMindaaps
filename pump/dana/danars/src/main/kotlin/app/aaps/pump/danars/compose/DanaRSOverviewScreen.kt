package app.gmaaps.pump.danars.compose

import androidx.compose.runtime.Composable
import app.gmaaps.pump.dana.DanaPump
import app.gmaaps.pump.dana.compose.DanaOverviewScreen

@Composable
fun DanaRSOverviewScreen(
    viewModel: DanaRSOverviewViewModel,
    danaPump: DanaPump
) {
    DanaOverviewScreen(
        viewModel = viewModel,
        danaPump = danaPump
    )
}

