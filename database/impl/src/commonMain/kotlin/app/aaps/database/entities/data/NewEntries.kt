package app.gmaaps.database.entities.data

import app.gmaaps.database.entities.APSResult
import app.gmaaps.database.entities.Bolus
import app.gmaaps.database.entities.BolusCalculatorResult
import app.gmaaps.database.entities.Carbs
import app.gmaaps.database.entities.EffectiveProfileSwitch
import app.gmaaps.database.entities.ExtendedBolus
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
import app.gmaaps.database.entities.VersionChange

data class NewEntries(
    val apsResults: List<APSResult>,
    val bolusCalculatorResults: List<BolusCalculatorResult>,
    val boluses: List<Bolus>,
    val carbs: List<Carbs>,
    val effectiveProfileSwitches: List<EffectiveProfileSwitch>,
    val extendedBoluses: List<ExtendedBolus>,
    val glucoseValues: List<GlucoseValue>,
    val runningModes: List<RunningMode>,
    val preferencesChanges: List<PreferenceChange>,
    val profileSwitches: List<ProfileSwitch>,
    val temporaryBasals: List<TemporaryBasal>,
    val temporaryTarget: List<TemporaryTarget>,
    val therapyEvents: List<TherapyEvent>,
    val totalDailyDoses: List<TotalDailyDose>,
    val versionChanges: List<VersionChange>,
    val heartRates: List<HeartRate>,
    val stepsCount: List<StepsCount>,
)

