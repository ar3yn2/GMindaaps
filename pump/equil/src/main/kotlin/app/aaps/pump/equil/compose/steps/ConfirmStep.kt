package app.gmaaps.pump.equil.compose.steps

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.gmaaps.core.ui.compose.AapsSpacing
import app.gmaaps.core.ui.compose.banner.ErrorBanner
import app.gmaaps.core.ui.compose.pump.WizardButton
import app.gmaaps.core.ui.compose.pump.WizardStepLayout
import app.gmaaps.pump.equil.R
import app.gmaaps.pump.equil.compose.EquilWizardStep
import app.gmaaps.pump.equil.compose.EquilWizardViewModel

@Composable
internal fun ConfirmStep(
    viewModel: EquilWizardViewModel,
    onCancel: () -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val hasBack = viewModel.hasPreviousStep(EquilWizardStep.CONFIRM)

    ConfirmStepContent(
        isLoading = isLoading,
        errorMessage = errorMessage,
        onFinish = { viewModel.startConfirm() },
        onBack = if (hasBack) ({ viewModel.moveToPreviousStep(EquilWizardStep.CONFIRM) }) else null,
        onCancel = onCancel
    )
}

/**
 * @see ConfirmStepPreview
 * @see ConfirmStepLoadingPreview
 * @see ConfirmStepErrorPreview
 */
@Composable
internal fun ConfirmStepContent(
    isLoading: Boolean,
    errorMessage: String?,
    onFinish: () -> Unit,
    onBack: (() -> Unit)?,
    onCancel: () -> Unit
) {
    WizardStepLayout(
        primaryButton = WizardButton(
            text = stringResource(app.gmaaps.core.ui.R.string.finish),
            onClick = onFinish,
            loading = isLoading
        ),
        secondaryButton = if (!isLoading) WizardButton(
            text = stringResource(if (onBack != null) app.gmaaps.core.ui.R.string.back else app.gmaaps.core.ui.R.string.cancel),
            onClick = onBack ?: onCancel
        ) else null
    ) {
        Text(
            text = stringResource(R.string.equil_install),
            style = MaterialTheme.typography.titleMedium
        )

        if (errorMessage != null) {
            Spacer(Modifier.height(AapsSpacing.medium))
            ErrorBanner(message = errorMessage)
        }
    }
}

