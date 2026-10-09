package app.gmaaps.ui.compose.scenes.wizard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import app.gmaaps.core.data.model.SceneAction
import app.gmaaps.core.data.model.TTPreset
import app.gmaaps.core.ui.CoreUiStrings
import app.gmaaps.core.ui.compose.AapsSpacing
import app.gmaaps.core.ui.compose.pump.WizardButton
import app.gmaaps.core.ui.compose.pump.WizardStepLayout
import app.gmaaps.core.ui.compose.stringResource
import app.gmaaps.ui.compose.scenes.TempTargetEditor

/**
 * @see TempTargetStepPreview
 */
@Composable
internal fun TempTargetStep(
    state: SceneWizardViewModel.WizardState,
    onToggle: (Boolean) -> Unit,
    onUpdate: (SceneAction) -> Unit,
    ttPresets: List<TTPreset>,
    formatBgWithUnits: (Double) -> String,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val canProceed = !state.ttEnabled || state.ttAction != null
    WizardStepLayout(
        secondaryButton = WizardButton(text = stringResource(CoreUiStrings.back), onClick = onBack),
        primaryButton = WizardButton(text = stringResource(CoreUiStrings.next), onClick = onNext, enabled = canProceed)
    ) {
        Text(
            text = stringResource(CoreUiStrings.temporary_target),
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = stringResource(CoreUiStrings.scene_wizard_tt_info),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        ActionToggle(
            label = stringResource(CoreUiStrings.scene_wizard_include_action, stringResource(CoreUiStrings.temporary_target)),
            checked = state.ttEnabled,
            onCheckedChange = onToggle
        )
        AnimatedVisibility(
            visible = state.ttEnabled,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(AapsSpacing.medium)) {
                TempTargetEditor(
                    action = state.ttAction,
                    onUpdate = onUpdate,
                    ttPresets = ttPresets,
                    formatBgWithUnits = formatBgWithUnits
                )
            }
        }
    }
}

