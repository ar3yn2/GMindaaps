package app.gmaaps.pump.carelevo.data.mapper

import app.gmaaps.pump.carelevo.data.model.entities.CarelevoAlarmInfoEntity
import app.gmaaps.pump.carelevo.domain.model.alarm.CarelevoAlarmInfo
import app.gmaaps.pump.carelevo.domain.type.AlarmType

fun CarelevoAlarmInfoEntity.transformToDomainModel(): CarelevoAlarmInfo =
    CarelevoAlarmInfo(
        alarmId = alarmId,
        alarmType = AlarmType.fromCode(alarmType),
        cause = cause,
        value = value,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isAcknowledged = acknowledged,
        occurrenceCount = occurrenceCount
    )

fun CarelevoAlarmInfo.transformToEntity(): CarelevoAlarmInfoEntity =
    CarelevoAlarmInfoEntity(
        alarmId = alarmId,
        alarmType = AlarmType.fromAlarmType(alarmType),
        cause = cause,
        value = value,
        createdAt = createdAt,
        updatedAt = updatedAt,
        acknowledged = isAcknowledged,
    )
