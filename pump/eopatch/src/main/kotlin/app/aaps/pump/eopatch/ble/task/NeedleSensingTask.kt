package app.gmaaps.pump.eopatch.ble.task

import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.pump.eopatch.alarm.AlarmCode
import app.gmaaps.pump.eopatch.alarm.IAlarmRegistry
import app.gmaaps.pump.eopatch.core.api.StartNeedleCheck
import app.gmaaps.pump.eopatch.core.api.UpdateConnection
import app.gmaaps.pump.eopatch.core.response.PatchBooleanResponse
import app.gmaaps.pump.eopatch.core.response.UpdateConnectionResponse
import app.gmaaps.pump.eopatch.vo.PatchState
import app.gmaaps.pump.eopatch.vo.PatchState.Companion.create
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.functions.Function

@Suppress("PropertyName")
@SingleIn(AppScope::class)
@Inject
class NeedleSensingTask(
    private val alarmRegistry: IAlarmRegistry
) : TaskBase(TaskFunc.NEEDLE_SENSING) {

    @Inject lateinit var startNeedleCheck: StartNeedleCheck
    @Inject lateinit var updateConnection: UpdateConnection

    fun start(): Single<Boolean> {
        return isReady()
            .concatMapSingle<PatchBooleanResponse>(Function { startNeedleCheck.start() })
            .doOnNext(Consumer { response: PatchBooleanResponse -> this.checkResponse(response) })
            .concatMapSingle<UpdateConnectionResponse>(Function { updateConnection.get() })
            .doOnNext(Consumer { response: UpdateConnectionResponse -> this.checkResponse(response) })
            .map<PatchState>(Function { updateConnectionResponse: UpdateConnectionResponse -> create(updateConnectionResponse.patchState, System.currentTimeMillis()) })
            .doOnNext(Consumer { v: PatchState -> this.onResponse(v) })
            .map<Boolean>(Function { patchState: PatchState -> !patchState.isNeedNeedleSensing })
            .firstOrError()
            .doOnError(Consumer { e: Throwable -> aapsLogger.error(LTag.PUMPCOMM, e.message ?: "NeedleSensingTask error") })
    }

    private fun onResponse(v: PatchState) {
        if (v.isNeedNeedleSensing) {
            alarmRegistry.add(AlarmCode.A016, 0, false).subscribe()
        } else {
            alarmRegistry.remove(AlarmCode.A016).subscribe()
        }
    }
}

