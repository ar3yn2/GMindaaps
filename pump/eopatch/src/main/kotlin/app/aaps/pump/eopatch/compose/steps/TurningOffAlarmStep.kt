package app.gmaaps.pump.eopatch.compose.steps

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import app.gmaaps.core.ui.compose.pump.WizardButton
import app.gmaaps.core.ui.compose.pump.WizardStepLayout
import app.gmaaps.pump.eopatch.R
import app.gmaaps.pump.eopatch.compose.EopatchPatchViewModel

/**
 * @see TurningOffAlarmStepPreview
 */
@Composable
fun TurningOffAlarmStep(viewModel: EopatchPatchViewModel) {
    WizardStepLayout(
        primaryButton = WizardButton(
            text = stringResource(app.gmaaps.core.ui.R.string.next),
            onClick = { viewModel.onConfirm() }
        )
    ) {
        Text(
            text = stringResource(R.string.patch_manually_turning_off_alarm_title),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(R.string.patch_manually_turning_off_alarm_desc_1),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(R.string.patch_manually_turning_off_alarm_desc_2),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(R.string.patch_manually_turning_off_alarm_step_1),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(R.string.patch_manually_turning_off_alarm_step_2),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

