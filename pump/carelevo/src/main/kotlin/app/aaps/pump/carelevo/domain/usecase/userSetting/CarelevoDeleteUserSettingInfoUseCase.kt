package app.gmaaps.pump.carelevo.domain.usecase.userSetting

import app.gmaaps.pump.carelevo.domain.model.ResponseResult
import app.gmaaps.pump.carelevo.domain.model.result.ResultSuccess
import app.gmaaps.pump.carelevo.domain.repository.CarelevoUserSettingInfoRepository
import app.gmaaps.pump.carelevo.domain.usecase.CarelevoUseCaseResponse
import dev.zacsweers.metro.Inject
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class CarelevoDeleteUserSettingInfoUseCase @Inject constructor(
    private val userSettingInfoRepository: CarelevoUserSettingInfoRepository
) {

    fun execute(): Single<ResponseResult<CarelevoUseCaseResponse>> {
        return Single.fromCallable {
            runCatching {
                val deleteResult = userSettingInfoRepository.deleteUserSettingInfo()
                if (!deleteResult) {
                    throw IllegalStateException("delete user setting info is failed")
                }
                ResultSuccess
            }.fold(
                onSuccess = {
                    ResponseResult.Success(it as CarelevoUseCaseResponse)
                },
                onFailure = {
                    ResponseResult.Error(it)
                }
            )
        }.subscribeOn(Schedulers.io())
    }
}
