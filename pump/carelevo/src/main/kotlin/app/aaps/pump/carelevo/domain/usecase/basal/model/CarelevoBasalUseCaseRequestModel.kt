package app.gmaaps.pump.carelevo.domain.usecase.basal.model

import app.gmaaps.core.interfaces.profile.Profile
import app.gmaaps.pump.carelevo.domain.usecase.CarelevoUseCaseRequest

data class SetBasalProgramRequestModel(
    val profile: Profile
) : CarelevoUseCaseRequest

data class StartTempBasalInfusionRequestModel(
    val isUnit: Boolean,
    val speed: Double? = null,
    val percent: Int? = null,
    val minutes: Int
) : CarelevoUseCaseRequest
