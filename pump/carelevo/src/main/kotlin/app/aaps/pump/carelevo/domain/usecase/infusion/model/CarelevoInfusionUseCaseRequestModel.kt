package app.gmaaps.pump.carelevo.domain.usecase.infusion.model

import app.gmaaps.pump.carelevo.domain.usecase.CarelevoUseCaseRequest

data class CarelevoPumpStopRequestModel(
    val durationMin: Int
) : CarelevoUseCaseRequest
