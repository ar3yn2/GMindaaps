package app.gmaaps.pump.carelevo.domain.usecase.userSetting

import app.gmaaps.pump.carelevo.domain.model.ResponseResult
import app.gmaaps.pump.carelevo.domain.repository.CarelevoUserSettingInfoRepository
import app.gmaaps.pump.carelevo.domain.usecase.CarelevoUseCaseResponse
import dev.zacsweers.metro.Inject
import io.reactivex.rxjava3.core.Observable
import kotlin.jvm.optionals.getOrNull

class CarelevoUserSettingInfoMonitorUseCase @Inject constructor(
    private val userSettingInfoRepository: CarelevoUserSettingInfoRepository
) {

    fun execute(): Observable<ResponseResult<CarelevoUseCaseResponse>> {
        return runCatching {
            userSettingInfoRepository.getUserSettingInfo()
        }.fold(
            onSuccess = { resultObservable ->
                resultObservable
                    .map { result ->
                        ResponseResult.Success(result.getOrNull())
                    }
            },
            onFailure = {
                Observable.just(ResponseResult.Error(it))
            }
        )
    }
}
