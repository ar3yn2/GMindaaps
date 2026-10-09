package app.gmaaps.pump.carelevo.data.mapper

import app.gmaaps.pump.carelevo.data.model.ble.ProtocolAdditionalBasalInfusionChangeRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolAdditionalBasalProgramSetRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolAdditionalPrimingRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolAlertMsgRptModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolAppAuthAckRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolAppAuthKeyAckRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolAppStatusRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolBasalInfusionChangeRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolBasalInfusionResumeRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolBasalInfusionStartRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolBasalProgramSetRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolBolusInfusionCancelRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolBuzzUsageChangeRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolCannulaInsertionAckRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolCannulaInsertionStatusRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolExtendBolusDelayRptModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolExtendBolusInfusionCancelRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolExtendBolusInfusionRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolImmeBolusInfusionRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolInfusionStatusInquiryRptModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolInfusionThresholdRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolMsgSolutionRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolNoticeMsgRptModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolNoticeThresholdRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolPatchAddressRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolPatchAlertAlarmSetRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolPatchBuzzInspectionRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolPatchDiscardRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolPatchExpiryExtendRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolPatchInformationInquiryDetailRptModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolPatchInformationInquiryRptModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolPatchInitRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolPatchOperationDataRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolPatchRecoveryRptModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolPatchThresholdSetRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolPumpResumeRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolPumpStopRptModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolPumpStopRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolSafetyCheckRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolSetTimeRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolTempBasalInfusionCancelRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolTempBasalInfusionRspModel
import app.gmaaps.pump.carelevo.data.model.ble.ProtocolWarningMsgRptModel
import app.gmaaps.pump.carelevo.domain.model.bt.AdditionalPrimingResponse
import app.gmaaps.pump.carelevo.domain.model.bt.AlertReportResponse
import app.gmaaps.pump.carelevo.domain.model.bt.AppAuthAckRptResponse
import app.gmaaps.pump.carelevo.domain.model.bt.AppAuthRptResponse
import app.gmaaps.pump.carelevo.domain.model.bt.CancelExtendBolusResponse
import app.gmaaps.pump.carelevo.domain.model.bt.CancelImmeBolusResponse
import app.gmaaps.pump.carelevo.domain.model.bt.CancelTempBasalProgramResponse
import app.gmaaps.pump.carelevo.domain.model.bt.CannulaInsertionAckResponse
import app.gmaaps.pump.carelevo.domain.model.bt.CannulaInsertionResponse
import app.gmaaps.pump.carelevo.domain.model.bt.CheckBuzzResponse
import app.gmaaps.pump.carelevo.domain.model.bt.ClearReportResponse
import app.gmaaps.pump.carelevo.domain.model.bt.DelayExtendBolusResponse
import app.gmaaps.pump.carelevo.domain.model.bt.NoticeReportResponse
import app.gmaaps.pump.carelevo.domain.model.bt.PatchInformationInquiryDetailResponse
import app.gmaaps.pump.carelevo.domain.model.bt.PatchInformationInquiryResponse
import app.gmaaps.pump.carelevo.domain.model.bt.RecoveryPatchResponse
import app.gmaaps.pump.carelevo.domain.model.bt.ResumeBasalProgramResponse
import app.gmaaps.pump.carelevo.domain.model.bt.ResumePumpResponse
import app.gmaaps.pump.carelevo.domain.model.bt.RetrieveAddressResponse
import app.gmaaps.pump.carelevo.domain.model.bt.RetrieveInfusionStatusResponse
import app.gmaaps.pump.carelevo.domain.model.bt.RetrieveOperationInfoResponse
import app.gmaaps.pump.carelevo.domain.model.bt.SafetyCheckResponse
import app.gmaaps.pump.carelevo.domain.model.bt.SetAlertAlarmModelResponse
import app.gmaaps.pump.carelevo.domain.model.bt.SetApplicationStatusResponse
import app.gmaaps.pump.carelevo.domain.model.bt.SetBasalProgramAdditionalResponse
import app.gmaaps.pump.carelevo.domain.model.bt.SetBasalProgramResponse
import app.gmaaps.pump.carelevo.domain.model.bt.SetBuzzModeResponse
import app.gmaaps.pump.carelevo.domain.model.bt.SetDiscardResponse
import app.gmaaps.pump.carelevo.domain.model.bt.SetExpiryExtendResponse
import app.gmaaps.pump.carelevo.domain.model.bt.SetInfusionThresholdResponse
import app.gmaaps.pump.carelevo.domain.model.bt.SetInitializeResponse
import app.gmaaps.pump.carelevo.domain.model.bt.SetThresholdNoticeResponse
import app.gmaaps.pump.carelevo.domain.model.bt.SetTimeResponse
import app.gmaaps.pump.carelevo.domain.model.bt.StartBasalProgramResponse
import app.gmaaps.pump.carelevo.domain.model.bt.StartExtendBolusResponse
import app.gmaaps.pump.carelevo.domain.model.bt.StartImmeBolusResponse
import app.gmaaps.pump.carelevo.domain.model.bt.StartTempBasalProgramResponse
import app.gmaaps.pump.carelevo.domain.model.bt.StopPumpReportResponse
import app.gmaaps.pump.carelevo.domain.model.bt.StopPumpResponse
import app.gmaaps.pump.carelevo.domain.model.bt.ThresholdSetResponse
import app.gmaaps.pump.carelevo.domain.model.bt.UpdateBasalProgramAdditionalResponse
import app.gmaaps.pump.carelevo.domain.model.bt.UpdateBasalProgramResponse
import app.gmaaps.pump.carelevo.domain.model.bt.WarningReportResponse

internal fun ProtocolSetTimeRspModel.transformToDomainModel() = SetTimeResponse(
    timestamp = timestamp,
    command = command,
    result = result
)

internal fun ProtocolSafetyCheckRspModel.transformToDomainModel() = SafetyCheckResponse(
    timestamp = timestamp,
    command = command,
    result = result,
    volume = insulinVolume,
    durationSeconds = durationSeconds
)

internal fun ProtocolAdditionalPrimingRspModel.transformToDomainModel() = AdditionalPrimingResponse(
    timestamp = timestamp,
    command = command,
    result = result
)

internal fun ProtocolPatchAlertAlarmSetRspModel.transformToDomainModel() = SetAlertAlarmModelResponse(
    timestamp = timestamp,
    command = command,
    result = result
)

internal fun ProtocolNoticeThresholdRspModel.transformToDomainModel() = SetThresholdNoticeResponse(
    timestamp = timestamp,
    command = command,
    result = result,
    type = type
)

internal fun ProtocolInfusionThresholdRspModel.transformToDomainModel() = SetInfusionThresholdResponse(
    timestamp = timestamp,
    command = command,
    result = result,
    type = type
)

internal fun ProtocolBuzzUsageChangeRspModel.transformToDomainModel() = SetBuzzModeResponse(
    timestamp = timestamp,
    command = command,
    result = result
)

internal fun ProtocolCannulaInsertionStatusRspModel.transformToDomainModel() = CannulaInsertionResponse(
    timestamp = timestamp,
    command = command,
    result = result
)

internal fun ProtocolCannulaInsertionAckRspModel.transformToDomainModel() = CannulaInsertionAckResponse(
    timestamp = timestamp,
    command = command,
    result = result
)

internal fun ProtocolPatchThresholdSetRspModel.transformToDomainModel() = ThresholdSetResponse(
    timestamp = timestamp,
    command = command,
    result = result
)

internal fun ProtocolPatchExpiryExtendRspModel.transformToDomainModel() = SetExpiryExtendResponse(
    timestamp = timestamp,
    command = command,
    result = result
)

internal fun ProtocolPatchInformationInquiryRptModel.transformToDomainModel() = PatchInformationInquiryResponse(
    timestamp = timestamp,
    command = command,
    result = result,
    // productCL = productCL,
    // productTY = productTY,
    // productMO = productMO,
    // processCO = processCO,
    // manufactureYE = manufactureYE,
    // manufactureMO = manufactureMO,
    // manufactureDA = manufactureDA,
    // manufactureLO = manufactureLO,
    // manufactureNO = manufactureNO
    serialNum = serialNum
)

internal fun ProtocolPatchInformationInquiryDetailRptModel.transformToDomainModel() = PatchInformationInquiryDetailResponse(
    timestamp = timestamp,
    command = command,
    result = result,
    firmVersion = firmVersion,
    bootDateTime = bootDateTime,
    modelName = modelName
)

internal fun ProtocolAppStatusRspModel.transformToDomainModel() = SetApplicationStatusResponse(
    timestamp = timestamp,
    command = command,
    status = status
)

internal fun ProtocolInfusionStatusInquiryRptModel.transformToDomainModel() = RetrieveInfusionStatusResponse(
    timestamp = timestamp,
    command = command,
    subId = subId,
    runningMinutes = patchRunningTime,
    remains = insulinRemains,
    infusedTotalBasalAmount = infusedTotalBasalAmount,
    infusedTotalBolusAmount = infusedTotalBolusAmount,
    pumpState = pumpState,
    mode = mode,
    infusedSetMinutes = infusedSetMin,
    currentInfusedProgramVolume = currentInfusedProgramVolume,
    realInfusedTime = realInfusedTime
)

internal fun ProtocolPumpStopRspModel.transformToDomainModel() = StopPumpResponse(
    timestamp = timestamp,
    command = command,
    result = result
)

internal fun ProtocolPumpStopRptModel.transformToDomainModel() = StopPumpReportResponse(
    timestamp = timestamp,
    command = command,
    result = result,
    mode = mode,
    causeId = subId,
    infusedBolusAmount = completedBolusInfusionVolume,
    unInfusedExtendBolusAmount = unInfusedExtendBolusVolume,
    temperature = temperature
)

internal fun ProtocolPumpResumeRspModel.transformToDomainModel() = ResumePumpResponse(
    timestamp = timestamp,
    command = command,
    result = result,
    mode = mode,
    causeId = subId
)

internal fun ProtocolPatchInitRspModel.transformToDomainModel() = SetInitializeResponse(
    timestamp = timestamp,
    command = command,
    mode = mode
)

internal fun ProtocolPatchDiscardRspModel.transformToDomainModel() = SetDiscardResponse(
    timestamp = timestamp,
    command = command,
    result = result
)

internal fun ProtocolPatchBuzzInspectionRspModel.transformToDomainModel() = CheckBuzzResponse(
    timestamp = timestamp,
    command = command,
    result = result
)

internal fun ProtocolPatchOperationDataRspModel.transformToDomainModel() = RetrieveOperationInfoResponse(
    timestamp = timestamp,
    command = command,
    mode = mode,
    pulseCnt = pulseCnt,
    totalNo = totalNo,
    count = count,
    useMinutes = useMin,
    remains = remains
)

internal fun ProtocolPatchAddressRspModel.transformToDomainModel() = RetrieveAddressResponse(
    timestamp = timestamp,
    command = command,
    // value = value,
    address = macAddress,
    checkSum = checkSum
)

internal fun ProtocolWarningMsgRptModel.transformToDomainModel() = WarningReportResponse(
    timestamp = timestamp,
    command = command,
    cause = cause,
    value = value
)

internal fun ProtocolAlertMsgRptModel.transformToDomainModel() = AlertReportResponse(
    timestamp = timestamp,
    command = command,
    cause = cause,
    value = value
)

internal fun ProtocolNoticeMsgRptModel.transformToDomainModel() = NoticeReportResponse(
    timestamp = timestamp,
    command = command,
    cause = cause,
    value = value
)

internal fun ProtocolMsgSolutionRspModel.transformToDomainModel() = ClearReportResponse(
    timestamp = timestamp,
    command = command,
    result = result,
    subId = subId,
    cause = cause
)

internal fun ProtocolBasalProgramSetRspModel.transformToDomainModel() = SetBasalProgramResponse(
    timestamp = timestamp,
    command = command,
    result = result
)

internal fun ProtocolAdditionalBasalProgramSetRspModel.transformToDomainModel() = SetBasalProgramAdditionalResponse(
    timestamp = timestamp,
    command = command,
    result = result
)

internal fun ProtocolBasalInfusionChangeRspModel.transformToDomainModel() = UpdateBasalProgramResponse(
    timestamp = timestamp,
    command = command,
    result = result
)

internal fun ProtocolAdditionalBasalInfusionChangeRspModel.transformToDomainModel() = UpdateBasalProgramAdditionalResponse(
    timestamp = timestamp,
    command = command,
    result = result
)

internal fun ProtocolTempBasalInfusionRspModel.transformToDomainModel() = StartTempBasalProgramResponse(
    timestamp = timestamp,
    command = command,
    result = result
)

internal fun ProtocolTempBasalInfusionCancelRspModel.transformToDomainModel() = CancelTempBasalProgramResponse(
    timestamp = timestamp,
    command = command,
    result = result
)

internal fun ProtocolBasalInfusionStartRspModel.transformToDomainModel() = StartBasalProgramResponse(
    timestamp = timestamp,
    command = command
)

internal fun ProtocolBasalInfusionResumeRspModel.transformToDomainModel() = ResumeBasalProgramResponse(
    timestamp = timestamp,
    command = command,
    segmentNo = segmentNo,
    infusionSpeed = infusionSpeed,
    infusionPeriod = infusionPeriod,
    insulinRemains = insulinRemains
)

internal fun ProtocolImmeBolusInfusionRspModel.transformToDomainModel() = StartImmeBolusResponse(
    timestamp = timestamp,
    command = command,
    result = result,
    actionId = actionId,
    expectedTime = expectedTime,
    remain = remains
)

internal fun ProtocolBolusInfusionCancelRspModel.transformToDomainModel() = CancelImmeBolusResponse(
    timestamp = timestamp,
    command = command,
    result = result,
    remains = insulinRemains,
    infusedAmount = infusedAmount
)

internal fun ProtocolExtendBolusInfusionRspModel.transformToDomainModel() = StartExtendBolusResponse(
    timestamp = timestamp,
    command = command,
    result = result,
    expectedTime = expectedTime
)

internal fun ProtocolExtendBolusInfusionCancelRspModel.transformToDomainModel() = CancelExtendBolusResponse(
    timestamp = timestamp,
    command = command,
    result = result,
    infusedAmount = infusedAmount
)

internal fun ProtocolExtendBolusDelayRptModel.transformToDomainModel() = DelayExtendBolusResponse(
    timestamp = timestamp,
    command = command,
    delayedAmount = delayedAmount,
    expectedTime = expectedTime
)

internal fun ProtocolPatchRecoveryRptModel.transformToDomainModel() = RecoveryPatchResponse(
    timestamp = timestamp,
    command = command
)

internal fun ProtocolAppAuthKeyAckRspModel.transformToDomainModel() = AppAuthRptResponse(
    timestamp = timestamp,
    command = command,
    value = value
)

internal fun ProtocolAppAuthAckRspModel.transformToDomainModel() = AppAuthAckRptResponse(
    timestamp = timestamp,
    command = command,
    result = result
)


