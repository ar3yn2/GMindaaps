package app.gmaaps.pump.carelevo.domain.usecase.bolus.model

import app.gmaaps.pump.carelevo.domain.usecase.CarelevoUseCaseRequest

data class StartImmeBolusInfusionRequestModel(
    val actionSeq: Int,
    val volume: Double
) : CarelevoUseCaseRequest

data class StartExtendBolusInfusionRequestModel(
    val volume: Double,
    val minutes: Int
) : CarelevoUseCaseRequest
