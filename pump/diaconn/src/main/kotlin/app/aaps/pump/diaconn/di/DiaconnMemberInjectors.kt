package app.gmaaps.pump.diaconn.di

import app.gmaaps.core.interfaces.di.FeatureMemberInjectors
import app.gmaaps.pump.diaconn.packet.AppCancelSettingPacket
import app.gmaaps.pump.diaconn.packet.AppCancelSettingResponsePacket
import app.gmaaps.pump.diaconn.packet.AppConfirmSettingPacket
import app.gmaaps.pump.diaconn.packet.AppConfirmSettingResponsePacket
import app.gmaaps.pump.diaconn.packet.BasalLimitInquirePacket
import app.gmaaps.pump.diaconn.packet.BasalLimitInquireResponsePacket
import app.gmaaps.pump.diaconn.packet.BasalPauseReportPacket
import app.gmaaps.pump.diaconn.packet.BasalPauseSettingPacket
import app.gmaaps.pump.diaconn.packet.BasalPauseSettingResponsePacket
import app.gmaaps.pump.diaconn.packet.BasalSettingPacket
import app.gmaaps.pump.diaconn.packet.BasalSettingReportPacket
import app.gmaaps.pump.diaconn.packet.BasalSettingResponsePacket
import app.gmaaps.pump.diaconn.packet.BatteryWarningReportPacket
import app.gmaaps.pump.diaconn.packet.BigAPSMainInfoInquirePacket
import app.gmaaps.pump.diaconn.packet.BigAPSMainInfoInquireResponsePacket
import app.gmaaps.pump.diaconn.packet.BigLogInquirePacket
import app.gmaaps.pump.diaconn.packet.BigLogInquireResponsePacket
import app.gmaaps.pump.diaconn.packet.BigMainInfoInquirePacket
import app.gmaaps.pump.diaconn.packet.BigMainInfoInquireResponsePacket
import app.gmaaps.pump.diaconn.packet.BolusSpeedInquirePacket
import app.gmaaps.pump.diaconn.packet.BolusSpeedInquireResponsePacket
import app.gmaaps.pump.diaconn.packet.BolusSpeedSettingPacket
import app.gmaaps.pump.diaconn.packet.BolusSpeedSettingReportPacket
import app.gmaaps.pump.diaconn.packet.BolusSpeedSettingResponsePacket
import app.gmaaps.pump.diaconn.packet.ConfirmReportPacket
import app.gmaaps.pump.diaconn.packet.DiaconnG8Packet
import app.gmaaps.pump.diaconn.packet.DisplayTimeInquirePacket
import app.gmaaps.pump.diaconn.packet.DisplayTimeInquireResponsePacket
import app.gmaaps.pump.diaconn.packet.DisplayTimeoutSettingPacket
import app.gmaaps.pump.diaconn.packet.DisplayTimeoutSettingResponsePacket
import app.gmaaps.pump.diaconn.packet.IncarnationInquirePacket
import app.gmaaps.pump.diaconn.packet.IncarnationInquireResponsePacket
import app.gmaaps.pump.diaconn.packet.InjectionBasalReportPacket
import app.gmaaps.pump.diaconn.packet.InjectionBasalSettingPacket
import app.gmaaps.pump.diaconn.packet.InjectionBasalSettingResponsePacket
import app.gmaaps.pump.diaconn.packet.InjectionBlockReportPacket
import app.gmaaps.pump.diaconn.packet.InjectionCancelSettingPacket
import app.gmaaps.pump.diaconn.packet.InjectionCancelSettingResponsePacket
import app.gmaaps.pump.diaconn.packet.InjectionExtendedBolusResultReportPacket
import app.gmaaps.pump.diaconn.packet.InjectionExtendedBolusSettingPacket
import app.gmaaps.pump.diaconn.packet.InjectionExtendedBolusSettingResponsePacket
import app.gmaaps.pump.diaconn.packet.InjectionMealSettingPacket
import app.gmaaps.pump.diaconn.packet.InjectionMealSettingResponsePacket
import app.gmaaps.pump.diaconn.packet.InjectionProgressReportPacket
import app.gmaaps.pump.diaconn.packet.InjectionSnackInquirePacket
import app.gmaaps.pump.diaconn.packet.InjectionSnackInquireResponsePacket
import app.gmaaps.pump.diaconn.packet.InjectionSnackResultReportPacket
import app.gmaaps.pump.diaconn.packet.InjectionSnackSettingPacket
import app.gmaaps.pump.diaconn.packet.InjectionSnackSettingResponsePacket
import app.gmaaps.pump.diaconn.packet.InsulinLackReportPacket
import app.gmaaps.pump.diaconn.packet.LanguageInquirePacket
import app.gmaaps.pump.diaconn.packet.LanguageInquireResponsePacket
import app.gmaaps.pump.diaconn.packet.LanguageSettingPacket
import app.gmaaps.pump.diaconn.packet.LanguageSettingResponsePacket
import app.gmaaps.pump.diaconn.packet.LogStatusInquirePacket
import app.gmaaps.pump.diaconn.packet.LogStatusInquireResponsePacket
import app.gmaaps.pump.diaconn.packet.RejectReportPacket
import app.gmaaps.pump.diaconn.packet.SerialNumInquirePacket
import app.gmaaps.pump.diaconn.packet.SerialNumInquireResponsePacket
import app.gmaaps.pump.diaconn.packet.SnackLimitInquirePacket
import app.gmaaps.pump.diaconn.packet.SnackLimitInquireResponsePacket
import app.gmaaps.pump.diaconn.packet.SoundInquirePacket
import app.gmaaps.pump.diaconn.packet.SoundInquireResponsePacket
import app.gmaaps.pump.diaconn.packet.SoundSettingPacket
import app.gmaaps.pump.diaconn.packet.SoundSettingResponsePacket
import app.gmaaps.pump.diaconn.packet.TempBasalInquirePacket
import app.gmaaps.pump.diaconn.packet.TempBasalInquireResponsePacket
import app.gmaaps.pump.diaconn.packet.TempBasalReportPacket
import app.gmaaps.pump.diaconn.packet.TempBasalSettingPacket
import app.gmaaps.pump.diaconn.packet.TempBasalSettingResponsePacket
import app.gmaaps.pump.diaconn.packet.TimeInquirePacket
import app.gmaaps.pump.diaconn.packet.TimeInquireResponsePacket
import app.gmaaps.pump.diaconn.packet.TimeReportPacket
import app.gmaaps.pump.diaconn.packet.TimeSettingPacket
import app.gmaaps.pump.diaconn.packet.TimeSettingResponsePacket
import app.gmaaps.pump.diaconn.service.DiaconnG8Service
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ClassKey
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.MembersInjector
import dev.zacsweers.metro.Provides

/**
 * Member injectors for the Diaconn protocol packets - the `@ContributesAndroidInjector` replacement.
 * The entries land in `AppRootGraph.contributedMemberInjectors` through `@ContributesTo`. That matters:
 * a graph extension would have to be named by `MetroGraphs`, which is compiled for follower builds where
 * this module does not exist. Contributing into the root instead needs no mention anywhere in `:app`.
 * One line per packet, the same as the `@ContributesAndroidInjector` line it replaces. If a packet is
 * missing, `DiaconnG8Packet`'s init throws by name rather than leaving fields null.
 */
@ContributesTo(AppScope::class)
@BindingContainer
object DiaconnMemberInjectors {

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(AppCancelSettingPacket::class)
    fun bindAppCancelSettingPacket(injector: MembersInjector<AppCancelSettingPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(AppCancelSettingResponsePacket::class)
    fun bindAppCancelSettingResponsePacket(injector: MembersInjector<AppCancelSettingResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(AppConfirmSettingPacket::class)
    fun bindAppConfirmSettingPacket(injector: MembersInjector<AppConfirmSettingPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(AppConfirmSettingResponsePacket::class)
    fun bindAppConfirmSettingResponsePacket(injector: MembersInjector<AppConfirmSettingResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(BasalLimitInquirePacket::class)
    fun bindBasalLimitInquirePacket(injector: MembersInjector<BasalLimitInquirePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(BasalLimitInquireResponsePacket::class)
    fun bindBasalLimitInquireResponsePacket(injector: MembersInjector<BasalLimitInquireResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(BasalPauseReportPacket::class)
    fun bindBasalPauseReportPacket(injector: MembersInjector<BasalPauseReportPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(BasalPauseSettingPacket::class)
    fun bindBasalPauseSettingPacket(injector: MembersInjector<BasalPauseSettingPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(BasalPauseSettingResponsePacket::class)
    fun bindBasalPauseSettingResponsePacket(injector: MembersInjector<BasalPauseSettingResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(BasalSettingPacket::class)
    fun bindBasalSettingPacket(injector: MembersInjector<BasalSettingPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(BasalSettingReportPacket::class)
    fun bindBasalSettingReportPacket(injector: MembersInjector<BasalSettingReportPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(BasalSettingResponsePacket::class)
    fun bindBasalSettingResponsePacket(injector: MembersInjector<BasalSettingResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(BatteryWarningReportPacket::class)
    fun bindBatteryWarningReportPacket(injector: MembersInjector<BatteryWarningReportPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(BigAPSMainInfoInquirePacket::class)
    fun bindBigAPSMainInfoInquirePacket(injector: MembersInjector<BigAPSMainInfoInquirePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(BigAPSMainInfoInquireResponsePacket::class)
    fun bindBigAPSMainInfoInquireResponsePacket(injector: MembersInjector<BigAPSMainInfoInquireResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(BigLogInquirePacket::class)
    fun bindBigLogInquirePacket(injector: MembersInjector<BigLogInquirePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(BigLogInquireResponsePacket::class)
    fun bindBigLogInquireResponsePacket(injector: MembersInjector<BigLogInquireResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(BigMainInfoInquirePacket::class)
    fun bindBigMainInfoInquirePacket(injector: MembersInjector<BigMainInfoInquirePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(BigMainInfoInquireResponsePacket::class)
    fun bindBigMainInfoInquireResponsePacket(injector: MembersInjector<BigMainInfoInquireResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(BolusSpeedInquirePacket::class)
    fun bindBolusSpeedInquirePacket(injector: MembersInjector<BolusSpeedInquirePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(BolusSpeedInquireResponsePacket::class)
    fun bindBolusSpeedInquireResponsePacket(injector: MembersInjector<BolusSpeedInquireResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(BolusSpeedSettingPacket::class)
    fun bindBolusSpeedSettingPacket(injector: MembersInjector<BolusSpeedSettingPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(BolusSpeedSettingReportPacket::class)
    fun bindBolusSpeedSettingReportPacket(injector: MembersInjector<BolusSpeedSettingReportPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(BolusSpeedSettingResponsePacket::class)
    fun bindBolusSpeedSettingResponsePacket(injector: MembersInjector<BolusSpeedSettingResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(ConfirmReportPacket::class)
    fun bindConfirmReportPacket(injector: MembersInjector<ConfirmReportPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(DiaconnG8Packet::class)
    fun bindDiaconnG8Packet(injector: MembersInjector<DiaconnG8Packet>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(DisplayTimeInquirePacket::class)
    fun bindDisplayTimeInquirePacket(injector: MembersInjector<DisplayTimeInquirePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(DisplayTimeInquireResponsePacket::class)
    fun bindDisplayTimeInquireResponsePacket(injector: MembersInjector<DisplayTimeInquireResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(DisplayTimeoutSettingPacket::class)
    fun bindDisplayTimeoutSettingPacket(injector: MembersInjector<DisplayTimeoutSettingPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(DisplayTimeoutSettingResponsePacket::class)
    fun bindDisplayTimeoutSettingResponsePacket(injector: MembersInjector<DisplayTimeoutSettingResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(IncarnationInquirePacket::class)
    fun bindIncarnationInquirePacket(injector: MembersInjector<IncarnationInquirePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(IncarnationInquireResponsePacket::class)
    fun bindIncarnationInquireResponsePacket(injector: MembersInjector<IncarnationInquireResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(InjectionBasalReportPacket::class)
    fun bindInjectionBasalReportPacket(injector: MembersInjector<InjectionBasalReportPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(InjectionBasalSettingPacket::class)
    fun bindInjectionBasalSettingPacket(injector: MembersInjector<InjectionBasalSettingPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(InjectionBasalSettingResponsePacket::class)
    fun bindInjectionBasalSettingResponsePacket(injector: MembersInjector<InjectionBasalSettingResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(InjectionBlockReportPacket::class)
    fun bindInjectionBlockReportPacket(injector: MembersInjector<InjectionBlockReportPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(InjectionCancelSettingPacket::class)
    fun bindInjectionCancelSettingPacket(injector: MembersInjector<InjectionCancelSettingPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(InjectionCancelSettingResponsePacket::class)
    fun bindInjectionCancelSettingResponsePacket(injector: MembersInjector<InjectionCancelSettingResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(InjectionExtendedBolusResultReportPacket::class)
    fun bindInjectionExtendedBolusResultReportPacket(injector: MembersInjector<InjectionExtendedBolusResultReportPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(InjectionExtendedBolusSettingPacket::class)
    fun bindInjectionExtendedBolusSettingPacket(injector: MembersInjector<InjectionExtendedBolusSettingPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(InjectionExtendedBolusSettingResponsePacket::class)
    fun bindInjectionExtendedBolusSettingResponsePacket(injector: MembersInjector<InjectionExtendedBolusSettingResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(InjectionMealSettingPacket::class)
    fun bindInjectionMealSettingPacket(injector: MembersInjector<InjectionMealSettingPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(InjectionMealSettingResponsePacket::class)
    fun bindInjectionMealSettingResponsePacket(injector: MembersInjector<InjectionMealSettingResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(InjectionProgressReportPacket::class)
    fun bindInjectionProgressReportPacket(injector: MembersInjector<InjectionProgressReportPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(InjectionSnackInquirePacket::class)
    fun bindInjectionSnackInquirePacket(injector: MembersInjector<InjectionSnackInquirePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(InjectionSnackInquireResponsePacket::class)
    fun bindInjectionSnackInquireResponsePacket(injector: MembersInjector<InjectionSnackInquireResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(InjectionSnackResultReportPacket::class)
    fun bindInjectionSnackResultReportPacket(injector: MembersInjector<InjectionSnackResultReportPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(InjectionSnackSettingPacket::class)
    fun bindInjectionSnackSettingPacket(injector: MembersInjector<InjectionSnackSettingPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(InjectionSnackSettingResponsePacket::class)
    fun bindInjectionSnackSettingResponsePacket(injector: MembersInjector<InjectionSnackSettingResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(InsulinLackReportPacket::class)
    fun bindInsulinLackReportPacket(injector: MembersInjector<InsulinLackReportPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(LanguageInquirePacket::class)
    fun bindLanguageInquirePacket(injector: MembersInjector<LanguageInquirePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(LanguageInquireResponsePacket::class)
    fun bindLanguageInquireResponsePacket(injector: MembersInjector<LanguageInquireResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(LanguageSettingPacket::class)
    fun bindLanguageSettingPacket(injector: MembersInjector<LanguageSettingPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(LanguageSettingResponsePacket::class)
    fun bindLanguageSettingResponsePacket(injector: MembersInjector<LanguageSettingResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(LogStatusInquirePacket::class)
    fun bindLogStatusInquirePacket(injector: MembersInjector<LogStatusInquirePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(LogStatusInquireResponsePacket::class)
    fun bindLogStatusInquireResponsePacket(injector: MembersInjector<LogStatusInquireResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(RejectReportPacket::class)
    fun bindRejectReportPacket(injector: MembersInjector<RejectReportPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(SerialNumInquirePacket::class)
    fun bindSerialNumInquirePacket(injector: MembersInjector<SerialNumInquirePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(SerialNumInquireResponsePacket::class)
    fun bindSerialNumInquireResponsePacket(injector: MembersInjector<SerialNumInquireResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(SnackLimitInquirePacket::class)
    fun bindSnackLimitInquirePacket(injector: MembersInjector<SnackLimitInquirePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(SnackLimitInquireResponsePacket::class)
    fun bindSnackLimitInquireResponsePacket(injector: MembersInjector<SnackLimitInquireResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(SoundInquirePacket::class)
    fun bindSoundInquirePacket(injector: MembersInjector<SoundInquirePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(SoundInquireResponsePacket::class)
    fun bindSoundInquireResponsePacket(injector: MembersInjector<SoundInquireResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(SoundSettingPacket::class)
    fun bindSoundSettingPacket(injector: MembersInjector<SoundSettingPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(SoundSettingResponsePacket::class)
    fun bindSoundSettingResponsePacket(injector: MembersInjector<SoundSettingResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(TempBasalInquirePacket::class)
    fun bindTempBasalInquirePacket(injector: MembersInjector<TempBasalInquirePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(TempBasalInquireResponsePacket::class)
    fun bindTempBasalInquireResponsePacket(injector: MembersInjector<TempBasalInquireResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(TempBasalReportPacket::class)
    fun bindTempBasalReportPacket(injector: MembersInjector<TempBasalReportPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(TempBasalSettingPacket::class)
    fun bindTempBasalSettingPacket(injector: MembersInjector<TempBasalSettingPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(TempBasalSettingResponsePacket::class)
    fun bindTempBasalSettingResponsePacket(injector: MembersInjector<TempBasalSettingResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(TimeInquirePacket::class)
    fun bindTimeInquirePacket(injector: MembersInjector<TimeInquirePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(TimeInquireResponsePacket::class)
    fun bindTimeInquireResponsePacket(injector: MembersInjector<TimeInquireResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(TimeReportPacket::class)
    fun bindTimeReportPacket(injector: MembersInjector<TimeReportPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(TimeSettingPacket::class)
    fun bindTimeSettingPacket(injector: MembersInjector<TimeSettingPacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(TimeSettingResponsePacket::class)
    fun bindTimeSettingResponsePacket(injector: MembersInjector<TimeSettingResponsePacket>): MembersInjector<*> = injector

    @Provides
    @FeatureMemberInjectors
    @IntoMap
    @ClassKey(DiaconnG8Service::class)
    fun bindDiaconnG8Service(injector: MembersInjector<DiaconnG8Service>): MembersInjector<*> = injector
}

