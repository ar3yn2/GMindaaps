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
 * @see RemoveStepPreview
 */
@Composable
fun RemoveStep(viewModel: EopatchPatchViewModel) {
    WizardStepLayout(
        primaryButton = WizardButton(
            text = stringResource(app.gmaaps.core.ui.R.string.confirm),
            onClick = { viewModel.onConfirm() }
        )
    ) {
        Text(
            text = stringResource(R.string.patch_discard_complete_title),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(R.string.patch_discard_complete_desc),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

