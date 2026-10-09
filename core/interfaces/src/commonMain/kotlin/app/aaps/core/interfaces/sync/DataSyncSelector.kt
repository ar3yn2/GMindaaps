package app.gmaaps.core.interfaces.sync

import app.gmaaps.core.data.model.BCR
import app.gmaaps.core.data.model.BS
import app.gmaaps.core.data.model.CA
import app.gmaaps.core.data.model.CAL
import app.gmaaps.core.data.model.DS
import app.gmaaps.core.data.model.EB
import app.gmaaps.core.data.model.EPS
import app.gmaaps.core.data.model.FD
import app.gmaaps.core.data.model.GV
import app.gmaaps.core.data.model.PS
import app.gmaaps.core.data.model.RM
import app.gmaaps.core.data.model.TB
import app.gmaaps.core.data.model.TE
import app.gmaaps.core.data.model.TT
import kotlinx.serialization.json.JsonObject

interface DataSyncSelector {

    interface DataPair {

        val value: Any
        val id: Long
        var confirmed: Boolean
    }

    data class PairTemporaryTarget(override val value: TT, override val id: Long, override var confirmed: Boolean = false) : DataPair
    data class PairGlucoseValue(override val value: GV, override val id: Long, override var confirmed: Boolean = false) : DataPair
    data class PairCalibrationEntry(override val value: CAL, override val id: Long, override var confirmed: Boolean = false) : DataPair
    data class PairTherapyEvent(override val value: TE, override val id: Long, override var confirmed: Boolean = false) : DataPair
    data class PairFood(override val value: FD, override val id: Long, override var confirmed: Boolean = false) : DataPair
    data class PairBolus(override val value: BS, override val id: Long, override var confirmed: Boolean = false) : DataPair
    data class PairCarbs(override val value: CA, override val id: Long, override var confirmed: Boolean = false) : DataPair
    data class PairBolusCalculatorResult(override val value: BCR, override val id: Long, override var confirmed: Boolean = false) : DataPair
    data class PairTemporaryBasal(override val value: TB, override val id: Long, override var confirmed: Boolean = false) : DataPair
    data class PairExtendedBolus(override val value: EB, override val id: Long, override var confirmed: Boolean = false) : DataPair
    data class PairProfileSwitch(override val value: PS, override val id: Long, override var confirmed: Boolean = false) : DataPair
    data class PairEffectiveProfileSwitch(override val value: EPS, override val id: Long, override var confirmed: Boolean = false) : DataPair
    data class PairRunningMode(override val value: RM, override val id: Long, override var confirmed: Boolean = false) : DataPair
    data class PairProfileStore(override val value: JsonObject, override val id: Long, override var confirmed: Boolean = false) : DataPair
    data class PairDeviceStatus(override val value: DS, override val id: Long, override var confirmed: Boolean = false) : DataPair

    fun queueSize(): Long

    suspend fun resetToNextFullSync()

    suspend fun doUpload()

    /**
     * This function called when new profile is received from NS
     * Plugin should update internal timestamp to not send Profile back as a new/updated
     *
     * @param timestamp received timestamp of profile
     *
     */
    fun profileReceived(timestamp: Long)
}
