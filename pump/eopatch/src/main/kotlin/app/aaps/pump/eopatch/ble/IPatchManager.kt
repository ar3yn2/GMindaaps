package app.gmaaps.pump.eopatch.ble

import app.gmaaps.core.interfaces.pump.DetailedBolusInfo
import app.gmaaps.pump.eopatch.core.scan.ScanList
import app.gmaaps.pump.eopatch.vo.PatchState
import io.reactivex.rxjava3.core.Single

interface IPatchManager {

    fun init()
    fun updatePatchState(state: PatchState)
    fun setConnection()
    fun patchActivation(timeout: Long): Single<Boolean>
    fun scan(timeout: Long): Single<ScanList>
    fun addBolusToHistory(originalDetailedBolusInfo: DetailedBolusInfo)
    fun changeBuzzerSetting()
    fun changeReminderSetting()
    fun checkActivationProcess()
}

