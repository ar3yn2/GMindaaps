package app.gmaaps.plugins.constraints.objectives.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.gmaaps.core.ui.CoreUiStrings
import app.gmaaps.core.ui.compose.ComposablePluginContent
import app.gmaaps.core.ui.compose.LocalSnackbarHostState
import app.gmaaps.core.ui.compose.ToolbarConfig
import app.gmaaps.core.ui.compose.dialogs.OkCancelDialog
import app.gmaaps.core.ui.compose.metroViewModel
import app.gmaaps.core.ui.compose.stringResource
import app.gmaaps.plugins.constraints.ConstraintsStrings

class ObjectivesComposeContent : ComposablePluginContent {

    @Composable
    override fun Render(
        setToolbarConfig: (ToolbarConfig) -> Unit,
        onNavigateBack: () -> Unit,
        onSettings: (() -> Unit)?
    ) {
        val viewModel: ObjectivesViewModel = metroViewModel()

        val state by viewModel.uiState.collectAsStateWithLifecycle()
        val scrollToIndex by viewModel.scrollToIndex.collectAsStateWithLifecycle()
        val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()

        // Snackbar
        val snackbarHostState = LocalSnackbarHostState.current
        LaunchedEffect(snackbarMessage) {
            snackbarMessage?.let { message ->
                snackbarHostState.showSnackbar(message)
                viewModel.onSnackbarShown()
            }
        }

        ObjectivesScreen(
            state = state,
            onFakeModeToggle = viewModel::onFakeModeToggle,
            onReset = viewModel::onReset,
            onStart = viewModel::onStart,
            onVerify = viewModel::onVerify,
            onRequestUnstart = viewModel::onRequestUnstart,
            onUnfinish = viewModel::onUnfinish,
            onShowLearned = viewModel::onShowLearned,
            onOpenExam = viewModel::onOpenExam,
            onInvokeUITask = viewModel::onInvokeUITask,
            scrollToIndex = scrollToIndex,
            onScrollHandled = viewModel::onScrollHandled
        )

        // NTP progress dialog
        state.ntpVerification?.let { ntpState ->
            NtpProgressDialog(state = ntpState)
        }

        // Confirm unstart dialog
        state.confirmUnstartDialog?.let { objectiveIndex ->
            OkCancelDialog(
                title = stringResource(CoreUiStrings.objectives),
                message = stringResource(ConstraintsStrings.doyouwantresetstart),
                onConfirm = { viewModel.onConfirmUnstart(objectiveIndex) },
                onDismiss = viewModel::onDismissUnstartDialog
            )
        }

        // Exam bottom sheet
        state.examSheet?.let { examState ->
            ExamBottomSheet(
                state = examState,
                onOptionToggle = viewModel::onExamOptionToggle,
                onVerify = viewModel::onExamVerify,
                onReset = viewModel::onExamReset,
                onNavigate = viewModel::onExamNavigate,
                onNextUnanswered = viewModel::onExamNextUnanswered,
                onDismiss = viewModel::onDismissExamSheet
            )
        }

        // Learned bottom sheet
        state.learnedSheet?.let { learnedState ->
            LearnedBottomSheet(
                state = learnedState,
                onDismiss = viewModel::onDismissLearnedSheet
            )
        }
    }
}

