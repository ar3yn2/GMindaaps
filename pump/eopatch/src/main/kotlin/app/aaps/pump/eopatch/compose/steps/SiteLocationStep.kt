package app.gmaaps.pump.eopatch.compose.steps

import androidx.compose.runtime.Composable
import app.gmaaps.core.ui.compose.siteRotation.SiteLocationWizardStep
import app.gmaaps.pump.eopatch.compose.EopatchPatchViewModel

@Composable
fun SiteLocationStep(viewModel: EopatchPatchViewModel) {
    SiteLocationWizardStep(host = viewModel)
}

