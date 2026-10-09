package app.gmaaps.pump.eopatch.ble.task

import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.rx.AapsSchedulers
import app.gmaaps.pump.eopatch.core.api.TempBasalScheduleStart
import app.gmaaps.pump.eopatch.core.response.TempBasalScheduleSetResponse
import app.gmaaps.pump.eopatch.vo.TempBasal
import app.gmaaps.pump.eopatch.vo.TempBasalManager
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.functions.Function

@SingleIn(AppScope::class)
@Inject
class StartTempBasalTask(
    private val tempBasalManager: TempBasalManager,
    private val aapsSchedulers: AapsSchedulers
) : TaskBase(TaskFunc.START_TEMP_BASAL) {

    @Inject lateinit var tempBasalScheduleStart: TempBasalScheduleStart

    fun start(tempBasal: TempBasal): Single<TempBasalScheduleSetResponse> {
        return isReady()
            .concatMapSingle<TempBasalScheduleSetResponse>(Function { tempBasalScheduleStart.start(tempBasal.durationMinutes, tempBasal.doseUnitPerHour, tempBasal.percent) })
            .doOnNext(Consumer { response: TempBasalScheduleSetResponse -> this.checkResponse(response) })
            .firstOrError()
            .observeOn(aapsSchedulers.io)
            .doOnSuccess(Consumer { onTempBasalStarted(tempBasal) })
            .doOnError(Consumer { e: Throwable -> aapsLogger.error(LTag.PUMPCOMM, e.message ?: "StartTempBasalTask error") })
    }

    private fun onTempBasalStarted(tempBasal: TempBasal) {
        tempBasalManager.updateBasalRunning(tempBasal)
        pm.flushTempBasalManager()
        enqueue(TaskFunc.UPDATE_CONNECTION)
    }

    @Throws(Exception::class) override fun preCondition() {
        checkPatchConnected()
    }
}

