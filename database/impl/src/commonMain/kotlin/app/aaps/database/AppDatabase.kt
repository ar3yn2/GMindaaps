package app.gmaaps.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
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
import app.gmaaps.database.entities.Bolus
import app.gmaaps.database.entities.BolusCalculatorResult
import app.gmaaps.database.entities.CalibrationEntry
import app.gmaaps.database.entities.Carbs
import app.gmaaps.database.entities.DeviceStatus
import app.gmaaps.database.entities.EffectiveProfileSwitch
import app.gmaaps.database.entities.ExtendedBolus
import app.gmaaps.database.entities.Food
import app.gmaaps.database.entities.GlucoseValue
import app.gmaaps.database.entities.HeartRate
import app.gmaaps.database.entities.PreferenceChange
import app.gmaaps.database.entities.ProfileSwitch
import app.gmaaps.database.entities.RunningMode
import app.gmaaps.database.entities.StepsCount
import app.gmaaps.database.entities.TemporaryBasal
import app.gmaaps.database.entities.TemporaryTarget
import app.gmaaps.database.entities.TherapyEvent
import app.gmaaps.database.entities.TotalDailyDose
import app.gmaaps.database.entities.UserEntry
import app.gmaaps.database.entities.VersionChange

const val DATABASE_VERSION = 36

@Database(
    version = DATABASE_VERSION,
    entities = [app.gmaaps.database.entities.APSResult::class, Bolus::class, BolusCalculatorResult::class, Carbs::class,
        EffectiveProfileSwitch::class, ExtendedBolus::class, GlucoseValue::class, ProfileSwitch::class,
        TemporaryBasal::class, TemporaryTarget::class, TherapyEvent::class, TotalDailyDose::class,
        PreferenceChange::class, VersionChange::class, UserEntry::class,
        Food::class, DeviceStatus::class, RunningMode::class, HeartRate::class, StepsCount::class,
        CalibrationEntry::class],
    exportSchema = true
)
// @ConstructedBy is what lets this class live in commonMain: Room generates the initialiser per target
// and the expect object below is where each one lands. On Android alone it would be optional.
@TypeConverters(Converters::class)
@ConstructedBy(AppDatabaseConstructor::class)
internal abstract class AppDatabase : RoomDatabase(), AppDatabaseDaos {

    abstract override val glucoseValueDao: GlucoseValueDao

    abstract override val therapyEventDao: TherapyEventDao

    abstract override val temporaryBasalDao: TemporaryBasalDao

    abstract override val bolusDao: BolusDao

    abstract override val extendedBolusDao: ExtendedBolusDao

    abstract override val totalDailyDoseDao: TotalDailyDoseDao

    abstract override val carbsDao: CarbsDao

    abstract override val temporaryTargetDao: TemporaryTargetDao

    abstract override val bolusCalculatorResultDao: BolusCalculatorResultDao

    abstract override val effectiveProfileSwitchDao: EffectiveProfileSwitchDao

    abstract override val profileSwitchDao: ProfileSwitchDao

    abstract override val apsResultDao: APSResultDao

    abstract override val versionChangeDao: VersionChangeDao

    abstract override val userEntryDao: UserEntryDao

    abstract override val preferenceChangeDao: PreferenceChangeDao

    abstract override val foodDao: FoodDao

    abstract override val deviceStatusDao: DeviceStatusDao

    abstract override val runningModeDao: RunningModeDao

    abstract override val heartRateDao: HeartRateDao

    abstract override val stepsCountDao: StepsCountDao

    abstract override val calibrationEntryDao: CalibrationEntryDao
}

// Room supplies the actual per target. It must be `expect`, because the generated initialiser is
// platform specific - that is the whole reason @ConstructedBy exists.
@Suppress("NO_ACTUAL_FOR_EXPECT")
internal expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {

    override fun initialize(): AppDatabase
}

