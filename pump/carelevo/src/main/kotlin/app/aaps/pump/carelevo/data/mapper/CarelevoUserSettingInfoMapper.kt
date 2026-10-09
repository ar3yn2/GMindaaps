package app.gmaaps.pump.carelevo.data.mapper

import app.gmaaps.pump.carelevo.data.model.entities.CarelevoUserSettingInfoEntity
import app.gmaaps.pump.carelevo.domain.model.userSetting.CarelevoUserSettingInfoDomainModel
import app.gmaaps.pump.carelevo.ext.parseIsoInstant

internal fun CarelevoUserSettingInfoEntity.transformToCarelevoUserSettingInfoDomainModel() = CarelevoUserSettingInfoDomainModel(
    createdAt = parseIsoInstant(createdAt),
    updatedAt = parseIsoInstant(updatedAt),
    lowInsulinNoticeAmount = lowInsulinNoticeAmount,
    maxBasalSpeed = maxBasalSpeed,
    maxBolusDose = maxBolusDose,
    needLowInsulinNoticeAmountSyncPatch = needLowInsulinNoticeAmountSyncPatch,
    needMaxBasalSpeedSyncPatch = needMaxBasalSpeedSyncPatch,
    needMaxBolusDoseSyncPatch = needMaxBolusDoseSyncPatch
)

internal fun CarelevoUserSettingInfoDomainModel.transformToCarelevoUserSettingInfoEntity() = CarelevoUserSettingInfoEntity(
    createdAt = createdAt.toString(),
    updatedAt = updatedAt.toString(),
    lowInsulinNoticeAmount = lowInsulinNoticeAmount,
    maxBasalSpeed = maxBasalSpeed,
    maxBolusDose = maxBolusDose,
    needLowInsulinNoticeAmountSyncPatch = needLowInsulinNoticeAmountSyncPatch,
    needMaxBasalSpeedSyncPatch = needMaxBasalSpeedSyncPatch,
    needMaxBolusDoseSyncPatch = needMaxBolusDoseSyncPatch
)
