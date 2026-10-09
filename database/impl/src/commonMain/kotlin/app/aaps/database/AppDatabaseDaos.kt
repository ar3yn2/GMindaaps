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

/**
 * Every DAO the database exposes, without the Room class that carries them.
 *
 * [DelegatedAppDatabase] and so every transaction only ever reads DAOs off the database. Taking this
 * interface instead of [AppDatabase] keeps that whole layer free of `RoomDatabase`, which is what lets
 * it live beside code that has no Android in it.
 */
internal interface AppDatabaseDaos {

    val glucoseValueDao: GlucoseValueDao
    val therapyEventDao: TherapyEventDao
    val temporaryBasalDao: TemporaryBasalDao
    val bolusDao: BolusDao
    val extendedBolusDao: ExtendedBolusDao
    val totalDailyDoseDao: TotalDailyDoseDao
    val carbsDao: CarbsDao
    val temporaryTargetDao: TemporaryTargetDao
    val bolusCalculatorResultDao: BolusCalculatorResultDao
    val effectiveProfileSwitchDao: EffectiveProfileSwitchDao
    val profileSwitchDao: ProfileSwitchDao
    val apsResultDao: APSResultDao
    val versionChangeDao: VersionChangeDao
    val userEntryDao: UserEntryDao
    val preferenceChangeDao: PreferenceChangeDao
    val foodDao: FoodDao
    val deviceStatusDao: DeviceStatusDao
    val runningModeDao: RunningModeDao
    val heartRateDao: HeartRateDao
    val stepsCountDao: StepsCountDao
    val calibrationEntryDao: CalibrationEntryDao
}

