package app.gmaaps.pump.danar.di

import app.gmaaps.core.interfaces.di.FeatureMemberInjectors
import app.gmaaps.pump.danar.comm.MessageBase
import app.gmaaps.pump.danar.comm.MsgBolusProgress
import app.gmaaps.pump.danar.comm.MsgBolusStart
import app.gmaaps.pump.danar.comm.MsgBolusStartWithSpeed
import app.gmaaps.pump.danar.comm.MsgBolusStop
import app.gmaaps.pump.danar.comm.MsgCheckValue
import app.gmaaps.pump.danar.comm.MsgError
import app.gmaaps.pump.danar.comm.MsgHistoryAlarm
import app.gmaaps.pump.danar.comm.MsgHistoryAll
import app.gmaaps.pump.danar.comm.MsgHistoryAllDone
import app.gmaaps.pump.danar.comm.MsgHistoryBasalHour
import app.gmaaps.pump.danar.comm.MsgHistoryBolus
import app.gmaaps.pump.danar.comm.MsgHistoryCarbo
import app.gmaaps.pump.danar.comm.MsgHistoryDailyInsulin
import app.gmaaps.pump.danar.comm.MsgHistoryDone
import app.gmaaps.pump.danar.comm.MsgHistoryError
import app.gmaaps.pump.danar.comm.MsgHistoryGlucose
import app.gmaaps.pump.danar.comm.MsgHistoryNew
import app.gmaaps.pump.danar.comm.MsgHistoryNewDone
import app.gmaaps.pump.danar.comm.MsgHistoryRefill
import app.gmaaps.pump.danar.comm.MsgHistorySuspend
import app.gmaaps.pump.danar.comm.MsgInitConnStatusBasic
import app.gmaaps.pump.danar.comm.MsgInitConnStatusBolus
import app.gmaaps.pump.danar.comm.MsgInitConnStatusOption
import app.gmaaps.pump.danar.comm.MsgInitConnStatusTime
import app.gmaaps.pump.danar.comm.MsgPCCommStart
import app.gmaaps.pump.danar.comm.MsgPCCommStop
import app.gmaaps.pump.danar.comm.MsgSetActivateBasalProfile
import app.gmaaps.pump.danar.comm.MsgSetBasalProfile
import app.gmaaps.pump.danar.comm.MsgSetCarbsEntry
import app.gmaaps.pump.danar.comm.MsgSetExtendedBolusStart
import app.gmaaps.pump.danar.comm.MsgSetExtendedBolusStop
import app.gmaaps.pump.danar.comm.MsgSetSingleBasalProfile
import app.gmaaps.pump.danar.comm.MsgSetTempBasalStart
import app.gmaaps.pump.danar.comm.MsgSetTempBasalStop
import app.gmaaps.pump.danar.comm.MsgSetTime
import app.gmaaps.pump.danar.comm.MsgSetUserOptions
import app.gmaaps.pump.danar.comm.MsgSettingActiveProfile
import app.gmaaps.pump.danar.comm.MsgSettingBasal
import app.gmaaps.pump.danar.comm.MsgSettingBasalProfileAll
import app.gmaaps.pump.danar.comm.MsgSettingGlucose
import app.gmaaps.pump.danar.comm.MsgSettingMaxValues
import app.gmaaps.pump.danar.comm.MsgSettingMeal
import app.gmaaps.pump.danar.comm.MsgSettingProfileRatios
import app.gmaaps.pump.danar.comm.MsgSettingProfileRatiosAll
import app.gmaaps.pump.danar.comm.MsgSettingPumpTime
import app.gmaaps.pump.danar.comm.MsgSettingShippingInfo
import app.gmaaps.pump.danar.comm.MsgSettingUserOptions
import app.gmaaps.pump.danar.comm.MsgStatus
import app.gmaaps.pump.danar.comm.MsgStatusBasic
import app.gmaaps.pump.danar.comm.MsgStatusBolusExtended
import app.gmaaps.pump.danar.comm.MsgStatusProfile
import app.gmaaps.pump.danar.comm.MsgStatusTempBasal
import app.gmaaps.pump.danar.services.DanaRExecutionService
import app.gmaaps.pump.danarkorean.comm.MsgCheckValueK
import app.gmaaps.pump.danarkorean.comm.MsgInitConnStatusBasicK
import app.gmaaps.pump.danarkorean.comm.MsgInitConnStatusBolusK
import app.gmaaps.pump.danarkorean.comm.MsgInitConnStatusTimeK
import app.gmaaps.pump.danarkorean.comm.MsgSettingBasalProfileAllK
import app.gmaaps.pump.danarkorean.comm.MsgSettingBasal_k
import app.gmaaps.pump.danarkorean.comm.MsgStatusBasic_k
import app.gmaaps.pump.danarkorean.comm.MsgStatus_k
import app.gmaaps.pump.danarkorean.services.DanaRKoreanExecutionService
import app.gmaaps.pump.danarv2.comm.MsgCheckValueV2
import app.gmaaps.pump.danarv2.comm.MsgHistoryEventsV2
import app.gmaaps.pump.danarv2.comm.MsgSetAPSTempBasalStartV2
import app.gmaaps.pump.danarv2.comm.MsgSetHistoryEntryV2
import app.gmaaps.pump.danarv2.comm.MsgStatusAPSV2
import app.gmaaps.pump.danarv2.services.DanaRv2ExecutionService
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ClassKey
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.MembersInjector
import dev.zacsweers.metro.Provides

/**
 * Member injectors for the DanaR messages - the `@ContributesAndroidInjector` replacement.
 *
 * Third of these, after Diaconn and Medtrum, and the same shape: `MessageBase` is built with `new` by
 * the execution service, so it fills its own fields from this map.
 *
 * The three execution services are in here too. Android constructs those rather than the graph, so
 * `MetroService` looks them up here in `onCreate` - the same map, reached from the application instead
 * of from an injector the caller holds. Only the concrete subclasses need an entry; nothing ever builds
 * the abstract base.
 */
@ContributesTo(AppScope::class)
@BindingContainer
object DanaRMemberInjectors {

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MessageBase::class)
    fun bindMessageBase(injector: MembersInjector<MessageBase>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgBolusProgress::class)
    fun bindMsgBolusProgress(injector: MembersInjector<MsgBolusProgress>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgBolusStart::class)
    fun bindMsgBolusStart(injector: MembersInjector<MsgBolusStart>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgBolusStartWithSpeed::class)
    fun bindMsgBolusStartWithSpeed(injector: MembersInjector<MsgBolusStartWithSpeed>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgBolusStop::class)
    fun bindMsgBolusStop(injector: MembersInjector<MsgBolusStop>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgCheckValue::class)
    fun bindMsgCheckValue(injector: MembersInjector<MsgCheckValue>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgCheckValueK::class)
    fun bindMsgCheckValueK(injector: MembersInjector<MsgCheckValueK>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgCheckValueV2::class)
    fun bindMsgCheckValueV2(injector: MembersInjector<MsgCheckValueV2>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgError::class)
    fun bindMsgError(injector: MembersInjector<MsgError>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgHistoryAlarm::class)
    fun bindMsgHistoryAlarm(injector: MembersInjector<MsgHistoryAlarm>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgHistoryAll::class)
    fun bindMsgHistoryAll(injector: MembersInjector<MsgHistoryAll>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgHistoryAllDone::class)
    fun bindMsgHistoryAllDone(injector: MembersInjector<MsgHistoryAllDone>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgHistoryBasalHour::class)
    fun bindMsgHistoryBasalHour(injector: MembersInjector<MsgHistoryBasalHour>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgHistoryBolus::class)
    fun bindMsgHistoryBolus(injector: MembersInjector<MsgHistoryBolus>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgHistoryCarbo::class)
    fun bindMsgHistoryCarbo(injector: MembersInjector<MsgHistoryCarbo>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgHistoryDailyInsulin::class)
    fun bindMsgHistoryDailyInsulin(injector: MembersInjector<MsgHistoryDailyInsulin>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgHistoryDone::class)
    fun bindMsgHistoryDone(injector: MembersInjector<MsgHistoryDone>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgHistoryError::class)
    fun bindMsgHistoryError(injector: MembersInjector<MsgHistoryError>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgHistoryEventsV2::class)
    fun bindMsgHistoryEventsV2(injector: MembersInjector<MsgHistoryEventsV2>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgHistoryGlucose::class)
    fun bindMsgHistoryGlucose(injector: MembersInjector<MsgHistoryGlucose>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgHistoryNew::class)
    fun bindMsgHistoryNew(injector: MembersInjector<MsgHistoryNew>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgHistoryNewDone::class)
    fun bindMsgHistoryNewDone(injector: MembersInjector<MsgHistoryNewDone>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgHistoryRefill::class)
    fun bindMsgHistoryRefill(injector: MembersInjector<MsgHistoryRefill>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgHistorySuspend::class)
    fun bindMsgHistorySuspend(injector: MembersInjector<MsgHistorySuspend>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgInitConnStatusBasic::class)
    fun bindMsgInitConnStatusBasic(injector: MembersInjector<MsgInitConnStatusBasic>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgInitConnStatusBasicK::class)
    fun bindMsgInitConnStatusBasicK(injector: MembersInjector<MsgInitConnStatusBasicK>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgInitConnStatusBolus::class)
    fun bindMsgInitConnStatusBolus(injector: MembersInjector<MsgInitConnStatusBolus>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgInitConnStatusBolusK::class)
    fun bindMsgInitConnStatusBolusK(injector: MembersInjector<MsgInitConnStatusBolusK>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgInitConnStatusOption::class)
    fun bindMsgInitConnStatusOption(injector: MembersInjector<MsgInitConnStatusOption>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgInitConnStatusTime::class)
    fun bindMsgInitConnStatusTime(injector: MembersInjector<MsgInitConnStatusTime>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgInitConnStatusTimeK::class)
    fun bindMsgInitConnStatusTimeK(injector: MembersInjector<MsgInitConnStatusTimeK>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgPCCommStart::class)
    fun bindMsgPCCommStart(injector: MembersInjector<MsgPCCommStart>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgPCCommStop::class)
    fun bindMsgPCCommStop(injector: MembersInjector<MsgPCCommStop>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSetAPSTempBasalStartV2::class)
    fun bindMsgSetAPSTempBasalStartV2(injector: MembersInjector<MsgSetAPSTempBasalStartV2>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSetActivateBasalProfile::class)
    fun bindMsgSetActivateBasalProfile(injector: MembersInjector<MsgSetActivateBasalProfile>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSetBasalProfile::class)
    fun bindMsgSetBasalProfile(injector: MembersInjector<MsgSetBasalProfile>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSetCarbsEntry::class)
    fun bindMsgSetCarbsEntry(injector: MembersInjector<MsgSetCarbsEntry>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSetExtendedBolusStart::class)
    fun bindMsgSetExtendedBolusStart(injector: MembersInjector<MsgSetExtendedBolusStart>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSetExtendedBolusStop::class)
    fun bindMsgSetExtendedBolusStop(injector: MembersInjector<MsgSetExtendedBolusStop>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSetHistoryEntryV2::class)
    fun bindMsgSetHistoryEntryV2(injector: MembersInjector<MsgSetHistoryEntryV2>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSetSingleBasalProfile::class)
    fun bindMsgSetSingleBasalProfile(injector: MembersInjector<MsgSetSingleBasalProfile>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSetTempBasalStart::class)
    fun bindMsgSetTempBasalStart(injector: MembersInjector<MsgSetTempBasalStart>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSetTempBasalStop::class)
    fun bindMsgSetTempBasalStop(injector: MembersInjector<MsgSetTempBasalStop>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSetTime::class)
    fun bindMsgSetTime(injector: MembersInjector<MsgSetTime>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSetUserOptions::class)
    fun bindMsgSetUserOptions(injector: MembersInjector<MsgSetUserOptions>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSettingActiveProfile::class)
    fun bindMsgSettingActiveProfile(injector: MembersInjector<MsgSettingActiveProfile>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSettingBasal::class)
    fun bindMsgSettingBasal(injector: MembersInjector<MsgSettingBasal>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSettingBasalProfileAll::class)
    fun bindMsgSettingBasalProfileAll(injector: MembersInjector<MsgSettingBasalProfileAll>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSettingBasalProfileAllK::class)
    fun bindMsgSettingBasalProfileAllK(injector: MembersInjector<MsgSettingBasalProfileAllK>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSettingBasal_k::class)
    fun bindMsgSettingBasalK(injector: MembersInjector<MsgSettingBasal_k>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgStatusBasic_k::class)
    fun bindMsgStatusBasicK(injector: MembersInjector<MsgStatusBasic_k>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgStatus_k::class)
    fun bindMsgStatusK(injector: MembersInjector<MsgStatus_k>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSettingGlucose::class)
    fun bindMsgSettingGlucose(injector: MembersInjector<MsgSettingGlucose>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSettingMaxValues::class)
    fun bindMsgSettingMaxValues(injector: MembersInjector<MsgSettingMaxValues>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSettingMeal::class)
    fun bindMsgSettingMeal(injector: MembersInjector<MsgSettingMeal>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSettingProfileRatios::class)
    fun bindMsgSettingProfileRatios(injector: MembersInjector<MsgSettingProfileRatios>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSettingProfileRatiosAll::class)
    fun bindMsgSettingProfileRatiosAll(injector: MembersInjector<MsgSettingProfileRatiosAll>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSettingPumpTime::class)
    fun bindMsgSettingPumpTime(injector: MembersInjector<MsgSettingPumpTime>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSettingShippingInfo::class)
    fun bindMsgSettingShippingInfo(injector: MembersInjector<MsgSettingShippingInfo>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgSettingUserOptions::class)
    fun bindMsgSettingUserOptions(injector: MembersInjector<MsgSettingUserOptions>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgStatus::class)
    fun bindMsgStatus(injector: MembersInjector<MsgStatus>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgStatusAPSV2::class)
    fun bindMsgStatusAPSV2(injector: MembersInjector<MsgStatusAPSV2>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgStatusBasic::class)
    fun bindMsgStatusBasic(injector: MembersInjector<MsgStatusBasic>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgStatusBolusExtended::class)
    fun bindMsgStatusBolusExtended(injector: MembersInjector<MsgStatusBolusExtended>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgStatusProfile::class)
    fun bindMsgStatusProfile(injector: MembersInjector<MsgStatusProfile>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(MsgStatusTempBasal::class)
    fun bindMsgStatusTempBasal(injector: MembersInjector<MsgStatusTempBasal>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(DanaRExecutionService::class)
    fun bindDanaRExecutionService(injector: MembersInjector<DanaRExecutionService>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(DanaRKoreanExecutionService::class)
    fun bindDanaRKoreanExecutionService(injector: MembersInjector<DanaRKoreanExecutionService>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(DanaRv2ExecutionService::class)
    fun bindDanaRv2ExecutionService(injector: MembersInjector<DanaRv2ExecutionService>): MembersInjector<*> = injector
}

