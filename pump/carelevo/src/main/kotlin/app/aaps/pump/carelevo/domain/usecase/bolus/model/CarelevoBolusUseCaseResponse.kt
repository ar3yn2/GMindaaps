package app.gmaaps.pump.carelevo.domain.usecase.bolus.model

import app.gmaaps.pump.carelevo.domain.usecase.CarelevoUseCaseResponse

data class CancelBolusInfusionResponseModel(
    val infusedAmount: Double,
) : CarelevoUseCaseResponse

data class StartImmeBolusInfusionResponseModel(
    val expectSec: Int
) : CarelevoUseCaseResponse
