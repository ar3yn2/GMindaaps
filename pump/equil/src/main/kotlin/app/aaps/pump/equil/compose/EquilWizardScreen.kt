package app.gmaaps.pump.equil.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.gmaaps.core.ui.compose.pump.ProfileGateWizardStep
import app.gmaaps.core.ui.compose.pump.WizardScreen
import app.gmaaps.pump.equil.R
import app.gmaaps.pump.equil.compose.steps.AirStep
import app.gmaaps.pump.equil.compose.steps.AssembleStep
import app.gmaaps.pump.equil.compose.steps.AttachStep
import app.gmaaps.pump.equil.compose.steps.BleScanStepWrapper
import app.gmaaps.pump.equil.compose.steps.ChangeInsulinStep
import app.gmaaps.pump.equil.compose.steps.ConfirmStep
import app.gmaaps.pump.equil.compose.steps.FillStep
import app.gmaaps.pump.equil.compose.steps.PasswordStep
import app.gmaaps.pump.equil.compose.steps.SelectInsulinStep
import app.gmaaps.pump.equil.compose.steps.SiteLocationStep
import app.gmaaps.pump.equil.compose.steps.UnpairConfirmStep
import app.gmaaps.pump.equil.compose.steps.UnpairDetachStep

@Composable
internal fun EquilWizardScreen(
    viewModel: EquilWizardViewModel,
    setToolbarConfig: ((app.gmaaps.core.ui.compose.ToolbarConfig) -> Unit)? = null
) {
    val wizardStep by viewModel.wizardStep.collectAsStateWithLifecycle()
    val totalSteps by viewModel.totalSteps.collectAsStateWithLifecycle()
    val currentStepIndex by viewModel.currentStepIndex.collectAsStateWithLifecycle()
    val canGoBack by viewModel.canGoBack.collectAsStateWithLifecycle()

    val titleResId by viewModel.titleResId.collectAsStateWithLifecycle()

    WizardScreen(
        currentStep = wizardStep,
        totalSteps = totalSteps,
        currentStepIndex = currentStepIndex,
        canGoBack = canGoBack,
        onBack = { viewModel.handleCancel() },
        cancelDialogTitle = stringResource(R.string.equil_common_wizard_exit_confirmation_title),
        cancelDialogText = stringResource(R.string.equil_common_wizard_exit_confirmation_text),
        title = stringResource(titleResId),
        setToolbarConfig = setToolbarConfig,
        stepContent = { step, onCancel ->
            when (step) {
                EquilWizardStep.PROFILE_GATE -> ProfileGateWizardStep(viewModel)
                EquilWizardStep.ASSEMBLE -> AssembleStep(viewModel, onCancel)
                EquilWizardStep.BLE_SCAN -> BleScanStepWrapper(viewModel, onCancel)
                EquilWizardStep.PASSWORD -> PasswordStep(viewModel, onCancel)
                EquilWizardStep.FILL -> FillStep(viewModel, onCancel)
                EquilWizardStep.ATTACH -> AttachStep(viewModel, onCancel)
                EquilWizardStep.AIR -> AirStep(viewModel, onCancel)
                EquilWizardStep.SELECT_INSULIN -> SelectInsulinStep(viewModel, onCancel)
                EquilWizardStep.SITE_LOCATION -> SiteLocationStep(viewModel, onCancel)
                EquilWizardStep.CONFIRM -> ConfirmStep(viewModel, onCancel)
                EquilWizardStep.CHANGE_INSULIN -> ChangeInsulinStep(viewModel, onCancel)
                EquilWizardStep.UNPAIR_DETACH -> UnpairDetachStep(viewModel, onCancel)
                EquilWizardStep.UNPAIR_CONFIRM -> UnpairConfirmStep(viewModel, onCancel)

                EquilWizardStep.CANCEL -> {} // Terminal, finish event emitted
            }
        }
    )
}

