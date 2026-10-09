package app.gmaaps.pump.equil.compose.steps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.gmaaps.core.ui.compose.insulin.SelectInsulin
import app.gmaaps.core.ui.compose.pump.WizardButton
import app.gmaaps.core.ui.compose.pump.WizardStepLayout
import app.gmaaps.pump.equil.compose.EquilWizardViewModel

@Composable
internal fun SelectInsulinStep(
    viewModel: EquilWizardViewModel,
    onCancel: () -> Unit
) {
    val availableInsulins by viewModel.availableInsulins.collectAsStateWithLifecycle()
    val selectedInsulin by viewModel.selectedInsulin.collectAsStateWithLifecycle()
    val activeInsulinLabel by viewModel.activeInsulinLabel.collectAsStateWithLifecycle()

    WizardStepLayout(
        primaryButton = WizardButton(
            text = stringResource(app.gmaaps.core.ui.R.string.next),
            onClick = { viewModel.moveToNextStep(app.gmaaps.pump.equil.compose.EquilWizardStep.SELECT_INSULIN) },
            enabled = selectedInsulin != null
        ),
        secondaryButton = WizardButton(
            text = stringResource(app.gmaaps.core.ui.R.string.cancel),
            onClick = onCancel
        )
    ) {
        SelectInsulin(
            availableInsulins = availableInsulins,
            selectedInsulin = selectedInsulin,
            activeInsulinLabel = activeInsulinLabel,
            onInsulinSelect = { viewModel.selectInsulin(it) },
            initialExpanded = true,
            concentrationDropDownEnabled = viewModel.concentrationEnabled
        )
    }
}

