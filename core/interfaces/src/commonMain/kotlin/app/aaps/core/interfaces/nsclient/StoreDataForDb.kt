package app.gmaaps.core.interfaces.nsclient

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

interface StoreDataForDb {

    fun addToGlucoseValues(payload: MutableList<GV>): Boolean
    fun addToCalibrationEntries(payload: MutableList<CAL>): Boolean
    fun addToBoluses(payload: BS): Boolean
    fun addToCarbs(payload: CA): Boolean
    fun addToTemporaryTargets(payload: TT): Boolean
    fun addToEffectiveProfileSwitches(payload: EPS): Boolean
    fun addToBolusCalculatorResults(payload: BCR): Boolean
    fun addToTherapyEvents(payload: TE): Boolean
    fun addToExtendedBoluses(payload: EB): Boolean
    fun addToTemporaryBasals(payload: TB): Boolean
    fun addToProfileSwitches(payload: PS): Boolean
    fun addToRunningModes(payload: RM): Boolean
    fun addToFoods(payload: MutableList<FD>): Boolean

    fun addToNsIdGlucoseValues(payload: GV): Boolean
    fun addToNsIdCalibrationEntries(payload: CAL): Boolean
    fun addToNsIdBoluses(payload: BS): Boolean
    fun addToNsIdCarbs(payload: CA): Boolean
    fun addToNsIdTemporaryTargets(payload: TT): Boolean
    fun addToNsIdEffectiveProfileSwitches(payload: EPS): Boolean
    fun addToNsIdBolusCalculatorResults(payload: BCR): Boolean
    fun addToNsIdTherapyEvents(payload: TE): Boolean
    fun addToNsIdExtendedBoluses(payload: EB): Boolean
    fun addToNsIdTemporaryBasals(payload: TB): Boolean
    fun addToNsIdProfileSwitches(payload: PS): Boolean
    fun addToNsIdRunningModes(payload: RM): Boolean
    fun addToNsIdDeviceStatuses(payload: DS): Boolean
    fun addToNsIdFoods(payload: FD): Boolean

    fun addToDeleteTreatment(payload: String): Boolean
    fun addToDeleteGlucoseValue(payload: String): Boolean

    suspend fun updateDeletedGlucoseValuesInDb()
    suspend fun storeTreatmentsToDb(fullSync: Boolean)
    suspend fun updateDeletedTreatmentsInDb()
    suspend fun storeGlucoseValuesToDb()
    suspend fun storeCalibrationEntriesToDb()
    suspend fun storeFoodsToDb()
    fun scheduleNsIdUpdate()
    suspend fun updateNsIds()

    /**
     * Fire-and-forget request methods. Multiple rapid calls coalesce into a single
     * collector run via a CONFLATED Channel — designed for WS callbacks where N
     * arrivals only need 1 store cycle to drain the buffer.
     */
    fun requestStoreGlucoseValues()
    fun requestStoreCalibrationEntries()
    fun requestStoreTreatments(fullSync: Boolean)
    fun requestStoreFoods()
    fun requestUpdateDeletedTreatments()
    fun requestUpdateDeletedGlucoseValues()
}
