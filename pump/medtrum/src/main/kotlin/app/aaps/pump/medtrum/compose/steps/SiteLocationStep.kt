package app.gmaaps.pump.medtrum.compose.steps

import androidx.compose.runtime.Composable
import app.gmaaps.core.ui.compose.siteRotation.SiteLocationWizardStep
import app.gmaaps.pump.medtrum.compose.MedtrumPatchViewModel

@Composable
fun SiteLocationStep(
    viewModel: MedtrumPatchViewModel,
    onCancel: () -> Unit
) {
    SiteLocationWizardStep(host = viewModel)
}

