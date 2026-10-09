package app.gmaaps.pump.eopatch.compose.steps

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import app.gmaaps.core.ui.compose.pump.WizardButton
import app.gmaaps.core.ui.compose.pump.WizardStepLayout
import app.gmaaps.pump.eopatch.R
import app.gmaaps.pump.eopatch.compose.EopatchPatchViewModel

/**
 * @see BasalScheduleStepPreview
 */
@Composable
fun BasalScheduleStep(viewModel: EopatchPatchViewModel) {
    LaunchedEffect(Unit) {
        viewModel.initPatchStep()
    }

    WizardStepLayout(
        primaryButton = WizardButton(
            text = stringResource(app.gmaaps.core.ui.R.string.finish),
            onClick = { viewModel.onConfirm() }
        )
    ) {
        Text(
            text = stringResource(R.string.patch_basal_schedule),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = viewModel.programEnabledMessage,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(R.string.patch_basal_schedule_desc_2),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

