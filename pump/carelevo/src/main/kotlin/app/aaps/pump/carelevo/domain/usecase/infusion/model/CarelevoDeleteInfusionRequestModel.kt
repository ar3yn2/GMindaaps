package app.gmaaps.pump.carelevo.domain.usecase.infusion.model

import app.gmaaps.pump.carelevo.domain.usecase.CarelevoUseCaseRequest

data class CarelevoDeleteInfusionRequestModel(
    val isDeleteTempBasal: Boolean,
    val isDeleteImmeBolus: Boolean,
    val isDeleteExtendBolus: Boolean
) : CarelevoUseCaseRequest

