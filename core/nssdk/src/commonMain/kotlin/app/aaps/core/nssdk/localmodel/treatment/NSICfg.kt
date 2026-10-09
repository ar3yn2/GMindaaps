package app.gmaaps.core.nssdk.localmodel.treatment

import kotlinx.serialization.Serializable

@Serializable
data class NSICfg(
    val insulinLabel: String,
    val insulinEndTime: Long,
    val insulinPeakTime: Long,
    val concentration: Double
)
