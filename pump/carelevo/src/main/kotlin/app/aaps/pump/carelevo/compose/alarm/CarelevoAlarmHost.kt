package app.gmaaps.pump.carelevo.compose.alarm

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.ui.compose.metroViewModel
import app.gmaaps.pump.carelevo.presentation.model.AlarmEvent
import app.gmaaps.pump.carelevo.presentation.viewmodel.CarelevoAlarmViewModel

/**
 * Hosts the two UI requests [app.gmaaps.pump.carelevo.common.CarelevoAlarmActionHandler] cannot fulfill
 * itself: launching the Bluetooth-enable intent, and a failure toast. Critical alarms surface through
 * the shared AAPS full-screen alarm ([app.gmaaps.core.interfaces.ui.UiInteraction.runAlarm], fired from
 * `CarelevoPumpPlugin.handleAlarms`) and every alarm's actual clear action lives on the persistent
 * notification card (`CarelevoAlarmNotifier.showTopNotification`) — this host renders nothing itself.
 */
@Composable
internal fun CarelevoAlarmHost(
    aapsLogger: AAPSLogger,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val viewModel: CarelevoAlarmViewModel = metroViewModel()

    val requestBtLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            aapsLogger.debug(LTag.PUMPCOMM, "bluetooth enabled for alarm")
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.event.collect { event ->
            when (event) {
                AlarmEvent.RequestBluetoothEnable -> {
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    requestBtLauncher.launch(enableBtIntent)
                }

                is AlarmEvent.ShowToastMessage    -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(event.messageRes)
                    )
                }

                else                              -> Unit
            }
        }
    }
}

