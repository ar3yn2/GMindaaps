package app.gmaaps.ui.compose.overview

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.gmaaps.core.ui.compose.StatusLevel
import app.gmaaps.core.ui.compose.preference.PreferenceSubScreenDef
import app.gmaaps.ui.compose.overview.statusLights.StatusItem

private val previewStatusItems = Triple(
    StatusItem(
        label = "Cannula",
        age = "16h",
        ageStatus = StatusLevel.NORMAL,
        level = null,
        icon = Icons.Default.Circle
    ),
    StatusItem(
        label = "Insulin",
        age = "16h",
        ageStatus = StatusLevel.NORMAL,
        level = "10 U",
        levelStatus = StatusLevel.NORMAL,
        icon = Icons.Default.Circle
    ),
    StatusItem(
        label = "Sensor",
        age = "3d",
        ageStatus = StatusLevel.WARNING,
        level = "82%",
        levelStatus = StatusLevel.NORMAL,
        icon = Icons.Default.Circle
    )
)

private val previewBatteryItem = StatusItem(
    label = "Battery",
    age = "2d",
    ageStatus = StatusLevel.NORMAL,
    level = "68%",
    levelStatus = StatusLevel.NORMAL,
    icon = Icons.Default.Circle
)

private val previewStatusLightsDef = PreferenceSubScreenDef(
    key = "preview",
    titleResId = 0
)

@Preview(showBackground = true, widthDp = 400)
@Composable
internal fun OverviewStatusSectionCollapsedPreview() {
    MaterialTheme {
        val (cannula, insulin, sensor) = previewStatusItems
        OverviewStatusSection(
            sensorStatus = sensor,
            insulinStatus = insulin,
            cannulaStatus = cannula,
            batteryStatus = previewBatteryItem,
            showFill = true,
            showPumpBatteryChange = true,
            onNavigate = {},
            statusLightsDef = previewStatusLightsDef,
            expanded = false,
            onExpandedChange = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
internal fun OverviewStatusSectionExpandedPreview() {
    MaterialTheme {
        val (cannula, insulin, sensor) = previewStatusItems
        OverviewStatusSection(
            sensorStatus = sensor,
            insulinStatus = insulin,
            cannulaStatus = cannula,
            batteryStatus = previewBatteryItem,
            showFill = true,
            showPumpBatteryChange = true,
            onNavigate = {},
            statusLightsDef = previewStatusLightsDef,
            expanded = true,
            onExpandedChange = {}
        )
    }
}

