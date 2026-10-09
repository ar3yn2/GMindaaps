package app.gmaaps.pump.eopatch.ble.task

import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.pump.eopatch.alarm.IAlarmRegistry
import app.gmaaps.pump.eopatch.core.api.GetErrorCodes
import app.gmaaps.pump.eopatch.core.response.AeCodeResponse
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.functions.Function
import java.util.concurrent.TimeUnit

@SingleIn(AppScope::class)
@Inject
class FetchAlarmTask(
    private val alarmRegistry: IAlarmRegistry
) : TaskBase(TaskFunc.FETCH_ALARM) {

    @Inject lateinit var alarmAlertErrorCodeGet: GetErrorCodes

    fun getPatchAlarm(): Single<AeCodeResponse> {
        return isReady()
            .concatMapSingle<AeCodeResponse>(Function { alarmAlertErrorCodeGet.get() })
            .doOnNext(Consumer { response: AeCodeResponse -> this.checkResponse(response) })
            .firstOrError()
            .doOnSuccess(Consumer { aeCodeResponse: AeCodeResponse -> alarmRegistry.add(aeCodeResponse.alarmCodes) })
            .doOnError(Consumer { e: Throwable -> aapsLogger.error(LTag.PUMPCOMM, e.message ?: "FetchAlarmTask error") })
    }

    @Synchronized override fun enqueue() {
        val ready = (disposable == null || disposable?.isDisposed == true)

        if (ready) {
            disposable = getPatchAlarm()
                .timeout(TASK_ENQUEUE_TIME_OUT, TimeUnit.SECONDS)
                .subscribe()
        }
    }
}

