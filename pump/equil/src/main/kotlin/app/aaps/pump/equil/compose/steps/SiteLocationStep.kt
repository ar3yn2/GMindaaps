package app.gmaaps.pump.equil.compose.steps

import androidx.compose.runtime.Composable
import app.gmaaps.core.ui.compose.siteRotation.SiteLocationWizardStep
import app.gmaaps.pump.equil.compose.EquilWizardViewModel

@Composable
internal fun SiteLocationStep(
    viewModel: EquilWizardViewModel,
    onCancel: () -> Unit
) {
    SiteLocationWizardStep(host = viewModel)
}

