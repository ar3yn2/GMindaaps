package app.gmaaps.plugins.automation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.core.ui.compose.consumeOverscroll
import app.gmaaps.core.ui.compose.stringResource
import app.gmaaps.plugins.automation.AutomationStrings
import app.gmaaps.plugins.automation.triggers.Trigger
import app.gmaaps.plugins.automation.triggers.TriggerAutosensValue
import app.gmaaps.plugins.automation.triggers.TriggerBTDevice
import app.gmaaps.plugins.automation.triggers.TriggerBg
import app.gmaaps.plugins.automation.triggers.TriggerBolusAgo
import app.gmaaps.plugins.automation.triggers.TriggerCOB
import app.gmaaps.plugins.automation.triggers.TriggerCannulaAge
import app.gmaaps.plugins.automation.triggers.TriggerDelta
import app.gmaaps.plugins.automation.triggers.TriggerHeartRate
import app.gmaaps.plugins.automation.triggers.TriggerInsulinAge
import app.gmaaps.plugins.automation.triggers.TriggerIob
import app.gmaaps.plugins.automation.triggers.TriggerLocation
import app.gmaaps.plugins.automation.triggers.TriggerPodChange
import app.gmaaps.plugins.automation.triggers.TriggerProfilePercent
import app.gmaaps.plugins.automation.triggers.TriggerPumpBatteryAge
import app.gmaaps.plugins.automation.triggers.TriggerPumpBatteryLevel
import app.gmaaps.plugins.automation.triggers.TriggerPumpLastConnection
import app.gmaaps.plugins.automation.triggers.TriggerRecurringTime
import app.gmaaps.plugins.automation.triggers.TriggerReservoirLevel
import app.gmaaps.plugins.automation.triggers.TriggerSensorAge
import app.gmaaps.plugins.automation.triggers.TriggerStepsCount
import app.gmaaps.plugins.automation.triggers.TriggerTempTarget
import app.gmaaps.plugins.automation.triggers.TriggerTempTargetValue
import app.gmaaps.plugins.automation.triggers.TriggerTime
import app.gmaaps.plugins.automation.triggers.TriggerTimeRange
import app.gmaaps.plugins.automation.triggers.TriggerWifiSsid
import kotlin.reflect.KClass

enum class TriggerCategory(val labelResId: TextRef) {
    Time(AutomationStrings.automation_category_time),
    Glucose(AutomationStrings.automation_category_glucose),
    Device(AutomationStrings.automation_category_device),
    Sensors(AutomationStrings.automation_category_sensors),
    Other(AutomationStrings.automation_category_other)
}

fun triggerCategoryOf(triggerClass: KClass<*>): TriggerCategory = when (triggerClass) {
    TriggerTime::class,
    TriggerRecurringTime::class,
    TriggerTimeRange::class -> TriggerCategory.Time

    TriggerBg::class,
    TriggerDelta::class,
    TriggerCOB::class,
    TriggerIob::class,
    TriggerAutosensValue::class,
    TriggerProfilePercent::class,
    TriggerTempTarget::class,
    TriggerTempTargetValue::class,
    TriggerBolusAgo::class -> TriggerCategory.Glucose

    TriggerCannulaAge::class,
    TriggerInsulinAge::class,
    TriggerReservoirLevel::class,
    TriggerPumpBatteryAge::class,
    TriggerPumpBatteryLevel::class,
    TriggerSensorAge::class,
    TriggerPodChange::class,
    TriggerPumpLastConnection::class -> TriggerCategory.Device

    TriggerHeartRate::class,
    TriggerStepsCount::class,
    TriggerLocation::class,
    TriggerWifiSsid::class,
    TriggerBTDevice::class -> TriggerCategory.Sensors

    else -> TriggerCategory.Other
}

data class TriggerOption(
    val className: String,
    val labelResId: TextRef,
    val icon: androidx.compose.ui.graphics.vector.ImageVector?,
    val prototype: Trigger,
    val category: TriggerCategory
) {

    companion object {

        fun from(trigger: Trigger): TriggerOption = TriggerOption(
            className = trigger::class.simpleName.orEmpty(),
            labelResId = trigger.friendlyName(),
            icon = trigger.composeIcon(),
            prototype = trigger,
            category = triggerCategoryOf(trigger::class)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ChooseTriggerSheet(
    options: List<TriggerOption>,
    onPick: (TriggerOption) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
                .consumeOverscroll()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(AutomationStrings.automation_choose_trigger),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(16.dp))
            val grouped = options.groupBy { it.category }
            TriggerCategory.entries.forEach { cat ->
                val items = grouped[cat].orEmpty()
                if (items.isEmpty()) return@forEach
                Text(
                    text = stringResource(cat.labelResId),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items.forEach { opt ->
                        AssistChip(
                            onClick = {
                                onPick(opt)
                                onDismiss()
                            },
                            label = { Text(stringResource(opt.labelResId)) },
                            leadingIcon = opt.icon?.let {
                                {
                                    Icon(
                                        imageVector = it,
                                        contentDescription = null,
                                        tint = opt.prototype.iconColor(),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

