package app.gmaaps.pump.carelevo.domain.usecase.userSetting

import app.gmaaps.pump.carelevo.domain.model.ResponseResult
import app.gmaaps.pump.carelevo.domain.model.result.ResultSuccess
import app.gmaaps.pump.carelevo.domain.model.userSetting.CarelevoUserSettingInfoDomainModel
import app.gmaaps.pump.carelevo.domain.repository.CarelevoUserSettingInfoRepository
import app.gmaaps.pump.carelevo.domain.usecase.CarelevoUseCaseRequest
import app.gmaaps.pump.carelevo.domain.usecase.CarelevoUseCaseResponse
import app.gmaaps.pump.carelevo.domain.usecase.userSetting.model.CarelevoUserSettingInfoRequestModel
import dev.zacsweers.metro.Inject
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class CarelevoCreateUserSettingInfoUseCase @Inject constructor(
    private val userSettingInfoRepository: CarelevoUserSettingInfoRepository
) {

    fun execute(request: CarelevoUseCaseRequest): Single<ResponseResult<CarelevoUseCaseResponse>> {
        return Single.fromCallable {
            runCatching {
                if (request !is CarelevoUserSettingInfoRequestModel) {
                    throw IllegalArgumentException("request is not CarelevoUserSettingInfoRequestModel")
                }

                val createResult = userSettingInfoRepository.updateUserSettingInfo(
                    CarelevoUserSettingInfoDomainModel(
                        lowInsulinNoticeAmount = request.lowInsulinNoticeAmount,
                        maxBasalSpeed = request.maxBasalSpeed,
                        maxBolusDose = request.maxBolusDose
                    )
                )

                if (!createResult) {
                    throw IllegalStateException("create user setting info is failed")
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
