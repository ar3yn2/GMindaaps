package app.gmaaps.pump.carelevo.coordinator

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.rx.AapsSchedulers
import app.gmaaps.pump.carelevo.domain.model.ResponseResult
import app.gmaaps.pump.carelevo.domain.usecase.userSetting.CarelevoDeleteUserSettingInfoUseCase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import java.util.concurrent.TimeUnit

/**
 * The preference-driven patch settings (max bolus, reminders, buzzer) are routed through the AAPS
 * CommandQueue as custom commands ([app.gmaaps.pump.carelevo.command.CarelevoActivationExecutor]); this
 * coordinator now only owns the shutdown-time settings clear.
 */
@SingleIn(AppScope::class)
class CarelevoSettingsCoordinator @Inject constructor(
    private val aapsLogger: AAPSLogger,
    private val aapsSchedulers: AapsSchedulers,
    private val deleteUserSettingInfoUseCase: CarelevoDeleteUserSettingInfoUseCase
) {

    // Best-effort DIRECT write, intentionally NOT queued: it runs from the plugin's onStop() during
    // shutdown, where reconnecting via the queue is unreliable and would block teardown. Reminder
    // settings on the patch are non-critical, so a missed clear on stop is acceptable.
    fun clearUserSettings(pluginDisposable: CompositeDisposable) {
        pluginDisposable += deleteUserSettingInfoUseCase.execute()
            .observeOn(aapsSchedulers.io)
            .subscribeOn(aapsSchedulers.io)
            .timeout(3000L, TimeUnit.MILLISECONDS)
            .subscribe { response ->
                when (response) {
                    is ResponseResult.Success -> {
                        aapsLogger.debug(LTag.PUMPCOMM, "deleteUserSettingInfo.success")
                    }

                    is ResponseResult.Error   -> {
                        aapsLogger.debug(LTag.PUMPCOMM, "deleteUserSettingInfo.responseError error=${response.e}")
                    }

                    else                      -> {
                        aapsLogger.debug(LTag.PUMPCOMM, "deleteUserSettingInfo.failure")
                    }
                }
            }
    }
}

