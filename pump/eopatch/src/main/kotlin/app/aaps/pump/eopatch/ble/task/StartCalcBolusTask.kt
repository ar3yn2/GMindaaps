package app.gmaaps.pump.eopatch.ble.task

import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.pump.DetailedBolusInfo
import app.gmaaps.pump.eopatch.core.api.BolusStart
import app.gmaaps.pump.eopatch.core.response.BaseResponse
import app.gmaaps.pump.eopatch.core.response.BolusResponse
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.functions.Function

@SingleIn(AppScope::class)
@Inject
class StartCalcBolusTask() : BolusTask(TaskFunc.START_CALC_BOLUS) {

    @Inject lateinit var nowBolusStart: BolusStart

    fun start(detailedBolusInfo: DetailedBolusInfo): Single<out BolusResponse> {
        return isReady().concatMapSingle(Function { startBolusImpl(detailedBolusInfo.insulin.toFloat()) })
            .doOnNext { response: BaseResponse -> this.checkResponse(response) }
            .firstOrError()
            .doOnSuccess { onSuccess(detailedBolusInfo.insulin.toFloat()) }
            .doOnError(Consumer { e: Throwable -> aapsLogger.error(LTag.PUMPCOMM, e.message ?: "StartCalcBolusTask error") })
    }

    private fun startBolusImpl(nowDoseU: Float): Single<out BolusResponse> {
        return nowBolusStart.start(nowDoseU)
    }

    private fun onSuccess(nowDoseU: Float) {
        onCalcBolusStarted(nowDoseU)
        enqueue(TaskFunc.UPDATE_CONNECTION)
    }

    @Throws(Exception::class) override fun preCondition() {
        checkPatchConnected()
    }
}

