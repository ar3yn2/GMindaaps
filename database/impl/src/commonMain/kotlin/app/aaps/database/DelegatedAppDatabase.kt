package app.gmaaps.database

import app.gmaaps.database.daos.APSResultDao
import app.gmaaps.database.daos.BolusCalculatorResultDao
import app.gmaaps.database.daos.BolusDao
import app.gmaaps.database.daos.CalibrationEntryDao
import app.gmaaps.database.daos.CarbsDao
import app.gmaaps.database.daos.DeviceStatusDao
import app.gmaaps.database.daos.EffectiveProfileSwitchDao
import app.gmaaps.database.daos.ExtendedBolusDao
import app.gmaaps.database.daos.FoodDao
import app.gmaaps.database.daos.GlucoseValueDao
import app.gmaaps.database.daos.HeartRateDao
import app.gmaaps.database.daos.PreferenceChangeDao
import app.gmaaps.database.daos.ProfileSwitchDao
import app.gmaaps.database.daos.RunningModeDao
import app.gmaaps.database.daos.StepsCountDao
import app.gmaaps.database.daos.TemporaryBasalDao
import app.gmaaps.database.daos.TemporaryTargetDao
import app.gmaaps.database.daos.TherapyEventDao
import app.gmaaps.database.daos.TotalDailyDoseDao
import app.gmaaps.database.daos.UserEntryDao
import app.gmaaps.database.daos.VersionChangeDao
import app.gmaaps.database.daos.delegated.DelegatedAPSResultDao
import app.gmaaps.database.daos.delegated.DelegatedBolusCalculatorResultDao
import app.gmaaps.database.daos.delegated.DelegatedBolusDao
import app.gmaaps.database.daos.delegated.DelegatedCalibrationEntryDao
import app.gmaaps.database.daos.delegated.DelegatedCarbsDao
import app.gmaaps.database.daos.delegated.DelegatedDeviceStatusDao
import app.gmaaps.database.daos.delegated.DelegatedEffectiveProfileSwitchDao
import app.gmaaps.database.daos.delegated.DelegatedExtendedBolusDao
import app.gmaaps.database.daos.delegated.DelegatedFoodDao
import app.gmaaps.database.daos.delegated.DelegatedGlucoseValueDao
import app.gmaaps.database.daos.delegated.DelegatedHeartRateDao
import app.gmaaps.database.daos.delegated.DelegatedPreferenceChangeDao
import app.gmaaps.database.daos.delegated.DelegatedProfileSwitchDao
import app.gmaaps.database.daos.delegated.DelegatedRunningModeDao
import app.gmaaps.database.daos.delegated.DelegatedStepsCountDao
import app.gmaaps.database.daos.delegated.DelegatedTemporaryBasalDao
import app.gmaaps.database.daos.delegated.DelegatedTemporaryTargetDao
import app.gmaaps.database.daos.delegated.DelegatedTherapyEventDao
import app.gmaaps.database.daos.delegated.DelegatedTotalDailyDoseDao
import app.gmaaps.database.daos.delegated.DelegatedUserEntryDao
import app.gmaaps.database.daos.delegated.DelegatedVersionChangeDao
import app.gmaaps.database.entities.interfaces.DBEntry

internal class DelegatedAppDatabase(val changes: MutableList<DBEntry>, private val database: AppDatabaseDaos) {

    val glucoseValueDao: GlucoseValueDao = DelegatedGlucoseValueDao(changes, database.glucoseValueDao)
    val therapyEventDao: TherapyEventDao = DelegatedTherapyEventDao(changes, database.therapyEventDao)
    val temporaryBasalDao: TemporaryBasalDao = DelegatedTemporaryBasalDao(changes, database.temporaryBasalDao)
    val bolusDao: BolusDao = DelegatedBolusDao(changes, database.bolusDao)
    val extendedBolusDao: ExtendedBolusDao = DelegatedExtendedBolusDao(changes, database.extendedBolusDao)
    val totalDailyDoseDao: TotalDailyDoseDao = DelegatedTotalDailyDoseDao(changes, database.totalDailyDoseDao)
    val carbsDao: CarbsDao = DelegatedCarbsDao(changes, database.carbsDao)
    val temporaryTargetDao: TemporaryTargetDao = DelegatedTemporaryTargetDao(changes, database.temporaryTargetDao)
    val bolusCalculatorResultDao: BolusCalculatorResultDao = DelegatedBolusCalculatorResultDao(changes, database.bolusCalculatorResultDao)
    val effectiveProfileSwitchDao: EffectiveProfileSwitchDao = DelegatedEffectiveProfileSwitchDao(changes, database.effectiveProfileSwitchDao)
    val profileSwitchDao: ProfileSwitchDao = DelegatedProfileSwitchDao(changes, database.profileSwitchDao)
    val apsResultDao: APSResultDao = DelegatedAPSResultDao(changes, database.apsResultDao)
    val versionChangeDao: VersionChangeDao = DelegatedVersionChangeDao(changes, database.versionChangeDao)
    val userEntryDao: UserEntryDao = DelegatedUserEntryDao(changes, database.userEntryDao)
    val preferenceChangeDao: PreferenceChangeDao = DelegatedPreferenceChangeDao(changes, database.preferenceChangeDao)
    val foodDao: FoodDao = DelegatedFoodDao(changes, database.foodDao)
    val deviceStatusDao: DeviceStatusDao = DelegatedDeviceStatusDao(changes, database.deviceStatusDao)
    val runningModeDao: RunningModeDao = DelegatedRunningModeDao(changes, database.runningModeDao)
    val heartRateDao: HeartRateDao = DelegatedHeartRateDao(changes, database.heartRateDao)
    val stepsCountDao: StepsCountDao = DelegatedStepsCountDao(changes, database.stepsCountDao)
    val calibrationEntryDao: CalibrationEntryDao = DelegatedCalibrationEntryDao(changes, database.calibrationEntryDao)
}

