package app.gmaaps.ui.compose.scenes.wizard

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalFocusManager
import app.gmaaps.core.data.configuration.Constants
import app.gmaaps.core.interfaces.InterfacesStrings
import app.gmaaps.core.ui.CoreUiStrings
import app.gmaaps.core.ui.compose.NumberInputRow
import app.gmaaps.core.ui.compose.formatMinutesAsDuration
import app.gmaaps.core.ui.compose.pump.WizardButton
import app.gmaaps.core.ui.compose.pump.WizardStepLayout
import app.gmaaps.core.ui.compose.stringResource

/**
 * @see DurationStepPreview
 */
@Composable
internal fun DurationStep(
    state: SceneWizardViewModel.WizardState,
    onSetDuration: (Int) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    WizardStepLayout(
        secondaryButton = WizardButton(
            text = stringResource(CoreUiStrings.back),
            onClick = { focusManager.clearFocus(); onBack() }
        ),
        primaryButton = WizardButton(
            text = stringResource(CoreUiStrings.next),
            onClick = { focusManager.clearFocus(); onNext() }
        )
    ) {
        Text(
            text = stringResource(CoreUiStrings.duration),
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = stringResource(CoreUiStrings.scene_wizard_duration_info),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        NumberInputRow(
            labelRef = CoreUiStrings.duration,
            value = state.durationMinutes.toDouble(),
            onValueChange = { onSetDuration(it.toInt()) },
            valueRange = Constants.SCENE_DURATION,
            step = 5.0,
            valueFormatRef = InterfacesStrings.mins,
            formatAsInt = true,
            displayValue = when {
                state.durationMinutes == 0 -> stringResource(CoreUiStrings.scene_duration_indefinite)
                else                       -> formatMinutesAsDuration(state.durationMinutes)
            }
        )
    }
}

