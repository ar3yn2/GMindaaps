package app.gmaaps.plugins.calibration.compose

import androidx.compose.runtime.Immutable
import app.gmaaps.core.data.model.CAL
import app.gmaaps.core.data.model.GlucoseUnit
import app.gmaaps.plugins.calibration.CalibrationFit

@Immutable
data class CalibrationUiState(
    val sessionStart: Long? = null,
    val warmUpEndsAt: Long? = null,
    val isInWarmUp: Boolean = false,
    val entries: List<CAL> = emptyList(),
    val fit: CalibrationFit? = null,
    val now: Long = 0L,
    val selectedEntryId: Long? = null,
    val glucoseUnit: GlucoseUnit = GlucoseUnit.MGDL
)

