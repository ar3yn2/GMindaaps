package app.gmaaps.plugins.sync.nsclientV3

import app.gmaaps.core.data.configuration.Constants
import app.gmaaps.core.data.model.FD
import app.gmaaps.core.data.model.GV
import app.gmaaps.core.data.model.ICfg
import app.gmaaps.core.data.model.TE
import app.gmaaps.core.data.time.T
import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.insulin.InsulinType
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.notifications.AlarmSound
import app.gmaaps.core.interfaces.notifications.NotificationAction
import app.gmaaps.core.interfaces.notifications.NotificationId
import app.gmaaps.core.interfaces.notifications.NotificationManager
import app.gmaaps.core.interfaces.nsclient.NSClientRepository
import app.gmaaps.core.interfaces.nsclient.StoreDataForDb
import app.gmaaps.core.interfaces.profile.ProfileFunction
import app.gmaaps.core.interfaces.profile.ProfileRepository
import app.gmaaps.core.interfaces.profile.ProfileStore
import app.gmaaps.core.interfaces.source.NSClientSource
import app.gmaaps.core.interfaces.sync.NsClient
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.keys.BooleanKey
import app.gmaaps.core.keys.BooleanNonKey
import app.gmaaps.core.keys.LongNonKey
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.nssdk.localmodel.entry.NSMbgV3
import app.gmaaps.core.nssdk.localmodel.entry.NSSgvV3
import app.gmaaps.core.nssdk.localmodel.food.NSFood
import app.gmaaps.core.nssdk.localmodel.treatment.NSBolus
import app.gmaaps.core.nssdk.localmodel.treatment.NSBolusWizard
import app.gmaaps.core.nssdk.localmodel.treatment.NSCarbs
import app.gmaaps.core.nssdk.localmodel.treatment.NSEffectiveProfileSwitch
import app.gmaaps.core.nssdk.localmodel.treatment.NSExtendedBolus
import app.gmaaps.core.nssdk.localmodel.treatment.NSOfflineEvent
import app.gmaaps.core.nssdk.localmodel.treatment.NSProfileSwitch
import app.gmaaps.core.nssdk.localmodel.treatment.NSTemporaryBasal
import app.gmaaps.core.nssdk.localmodel.treatment.NSTemporaryTarget
import app.gmaaps.core.nssdk.localmodel.treatment.NSTherapyEvent
import app.gmaaps.core.nssdk.localmodel.treatment.NSTreatment
import app.gmaaps.core.ui.CoreUiStrings
import app.gmaaps.plugins.sync.nsclientV3.extensions.toBolus
import app.gmaaps.plugins.sync.nsclientV3.extensions.toBolusCalculatorResult
import app.gmaaps.plugins.sync.nsclientV3.extensions.toCAL
import app.gmaaps.plugins.sync.nsclientV3.extensions.toCarbs
import app.gmaaps.plugins.sync.nsclientV3.extensions.toEffectiveProfileSwitch
import app.gmaaps.plugins.sync.nsclientV3.extensions.toExtendedBolus
import app.gmaaps.plugins.sync.nsclientV3.extensions.toFood
import app.gmaaps.plugins.sync.nsclientV3.extensions.toGV
import app.gmaaps.plugins.sync.nsclientV3.extensions.toProfileSwitch
import app.gmaaps.plugins.sync.nsclientV3.extensions.toRunningMode
import app.gmaaps.plugins.sync.nsclientV3.extensions.toTemporaryBasal
import app.gmaaps.plugins.sync.nsclientV3.extensions.toTemporaryTarget
import app.gmaaps.plugins.sync.nsclientV3.extensions.toTherapyEvent
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.serialization.json.JsonObject

@SingleIn(AppScope::class)
@Inject
class NsIncomingDataProcessor(
    private val aapsLogger: AAPSLogger,
    private val nsClientSource: NSClientSource,
    private val preferences: Preferences,
    private val dateUtil: DateUtil,
    private val nsClient: NsClient,
    private val profileFunction: ProfileFunction,
    private val profileRepository: ProfileRepository,
    private val storeDataForDb: StoreDataForDb,
    private val config: Config,
    private val profileStoreProvider: () -> ProfileStore,
    private val notificationManager: NotificationManager,
    private val nsClientRepository: NSClientRepository
) {

    /**
     * Insulin to stamp on an incoming record that carries none — uploaded by an older AAPS or by a non-AAPS
     * client. `BS.iCfg` / `PS.iCfg` / `EPS.iCfg` are non-null, so the alternative to guessing here is discarding
     * the record, and losing the user's history on a restore is the worse outcome.
     *
     * The running profile's insulin is the best available answer, but it is still only a guess: for a record
     * from months ago the correct value is whatever was in force *then*, which would need a suspending,
     * per-record `getProfile(timestamp)` — and on the full sync of a fresh install that history has not been
     * imported yet anyway. [OREF_RAPID_ACTING] backstops exactly that case (nothing in force, because the very
     * profile switches that would establish it are arriving in this same batch); it matches what a fresh
     * install seeds. Read per record, not hoisted, so records later in a batch pick up a profile switch that
     * landed earlier in it.
     */
    private fun fallbackICfg(): ICfg = profileFunction.runningICfg.value ?: InsulinType.OREF_RAPID_ACTING.iCfg

    /**
     * Preprocess list of SGVs
     *
     * @return true if there was an accepted SGV
     */
    fun processSgvs(sgvs: Any, doFullSync: Boolean): Boolean {
        // Objective0
        preferences.put(BooleanNonKey.ObjectivesBgIsAvailableInNs, true)

        if (!nsClientSource.isEnabled() && !preferences.get(BooleanKey.NsClientAcceptCgmData) && !doFullSync) return false

        var latestDateInReceivedData: Long = 0
        aapsLogger.debug(LTag.NSCLIENT, "Received NS Data: $sgvs")
        val glucoseValues = mutableListOf<GV>()

        if (sgvs is List<*>) { // V3 client

            for (i in 0 until sgvs.size) {
                val sgv = (sgvs[i] as NSSgvV3).toGV()
                if (sgv.timestamp < dateUtil.now() + T.mins(1).msecs() && sgv.timestamp > latestDateInReceivedData) {
                    latestDateInReceivedData = sgv.timestamp
                    glucoseValues += sgv
                } else
                    aapsLogger.debug(LTag.NSCLIENT, "Ignoring record with wrong timestamp: $sgv")
            }
        }
        if (glucoseValues.isNotEmpty()) {
            nsClient.updateLatestBgReceivedIfNewer(latestDateInReceivedData)
            // Was that sgv more less 5 mins ago ?
            if (T.msecs(dateUtil.now() - latestDateInReceivedData).mins() < 5L) {
                notificationManager.dismiss(NotificationId.NS_ALARM)
                notificationManager.dismiss(NotificationId.NS_URGENT_ALARM)
            }
            storeDataForDb.addToGlucoseValues(glucoseValues)
        }
        return glucoseValues.isNotEmpty()
    }

    /**
     * Preprocess list of calibration mbg entries (AAPS-marked). Used on follower instances so they
     * can re-fit the same calibration curve as the master.
     *
     * @return true if there was an accepted calibration entry
     */
    fun processCalibrations(mbgs: List<NSMbgV3>, doFullSync: Boolean): Boolean {
        if (!config.AAPSCLIENT && !preferences.get(BooleanKey.NsClientAcceptCgmData) && !doFullSync) return false
        if (mbgs.isEmpty()) return false
        val calibrationEntries = mbgs.map { it.toCAL() }
        aapsLogger.debug(LTag.NSCLIENT, "Received NS calibrations: ${calibrationEntries.size}")
        storeDataForDb.addToCalibrationEntries(calibrationEntries.toMutableList())
        return calibrationEntries.isNotEmpty()
    }

    /**
     * Preprocess list of treatments
     *
     * @return true if there was an accepted treatment
     */
    fun processTreatments(treatments: List<NSTreatment>, doFullSync: Boolean): Boolean {
        try {
            var latestDateInReceivedData: Long = 0
            for (treatment in treatments) {
                aapsLogger.debug(LTag.NSCLIENT, "Received NS treatment: $treatment")
                val date = treatment.date ?: continue
                if (date > latestDateInReceivedData) latestDateInReceivedData = date

                when (treatment) {
                    is NSBolus                  ->
                        if (preferences.get(BooleanKey.NsClientAcceptInsulin) || config.AAPSCLIENT || doFullSync)
                            storeDataForDb.addToBoluses(treatment.toBolus(fallbackICfg()))

                    is NSCarbs                  ->
                        if (preferences.get(BooleanKey.NsClientAcceptCarbs) || config.AAPSCLIENT || doFullSync)
                            storeDataForDb.addToCarbs(treatment.toCarbs())

                    is NSTemporaryTarget        ->
                        if (preferences.get(BooleanKey.NsClientAcceptTempTarget) || config.AAPSCLIENT || doFullSync) {
                            if (treatment.duration > 0L) {
                                // not ending event
                                if (treatment.targetBottomAsMgdl() !in Constants.TT_RANGE_MGDL
                                    || treatment.targetTopAsMgdl() !in Constants.TT_RANGE_MGDL
                                    || treatment.targetBottomAsMgdl() > treatment.targetTopAsMgdl()
                                ) {
                                    aapsLogger.debug(LTag.NSCLIENT, "Ignored TemporaryTarget $treatment")
                                    continue
                                }
                            }
                            storeDataForDb.addToTemporaryTargets(treatment.toTemporaryTarget())
                        }

                    is NSTemporaryBasal         ->
                        if (preferences.get(BooleanKey.NsClientAcceptTbrEb) || config.AAPSCLIENT || doFullSync)
                            storeDataForDb.addToTemporaryBasals(treatment.toTemporaryBasal())

                    is NSEffectiveProfileSwitch ->
                        if (preferences.get(BooleanKey.NsClientAcceptProfileSwitch) || config.AAPSCLIENT || doFullSync) {
                            treatment.toEffectiveProfileSwitch(dateUtil, fallbackICfg())?.let { effectiveProfileSwitch ->
                                storeDataForDb.addToEffectiveProfileSwitches(effectiveProfileSwitch)
                            }
                        }

                    is NSProfileSwitch          ->
                        if (preferences.get(BooleanKey.NsClientAcceptProfileSwitch) || config.AAPSCLIENT || doFullSync) {
                            treatment.toProfileSwitch(profileRepository, dateUtil, fallbackICfg())?.let { profileSwitch ->
                                storeDataForDb.addToProfileSwitches(profileSwitch)
                            }
                        }

                    is NSBolusWizard            ->
                        treatment.toBolusCalculatorResult()?.let { bolusCalculatorResult ->
                            storeDataForDb.addToBolusCalculatorResults(bolusCalculatorResult)
                        }

                    is NSTherapyEvent           ->
                        if (preferences.get(BooleanKey.NsClientAcceptTherapyEvent) || config.AAPSCLIENT || doFullSync)
                            treatment.toTherapyEvent().let { therapyEvent ->
                                storeDataForDb.addToTherapyEvents(therapyEvent)
                                if (therapyEvent.type == TE.Type.ANNOUNCEMENT &&
                                    preferences.get(BooleanKey.NsClientNotificationsFromAnnouncements) &&
                                    therapyEvent.timestamp + T.mins(60).msecs() > dateUtil.now()
                                )
                                    notificationManager.post(
                                        id = NotificationId.NS_ANNOUNCEMENT,
                                        text = therapyEvent.note ?: "",
                                        validTo = dateUtil.now() + T.mins(60).msecs(),
                                        sound = AlarmSound.ALARM,
                                        actions = listOf(NotificationAction(CoreUiStrings.snooze) { })
                                    )
                            }

                    is NSOfflineEvent           ->
                        if (preferences.get(BooleanKey.NsClientAcceptRunningMode) || config.AAPSCLIENT || doFullSync)
                            treatment.toRunningMode().let { runningMode ->
                                storeDataForDb.addToRunningModes(runningMode)
                            }

                    is NSExtendedBolus          ->
                        if (preferences.get(BooleanKey.NsClientAcceptTbrEb) || config.AAPSCLIENT || doFullSync)
                            treatment.toExtendedBolus().let { extendedBolus ->
                                storeDataForDb.addToExtendedBoluses(extendedBolus)
                            }
                }
            }
            if (latestDateInReceivedData > 0)
                nsClient.updateLatestTreatmentReceivedIfNewer(latestDateInReceivedData)
            return latestDateInReceivedData > 0
        } catch (error: Exception) {
            aapsLogger.error("Error: ", error)
            nsClientRepository.addLog("◄ ERROR", error.message)
        }
        return false
    }

    fun processFood(data: Any) {
        aapsLogger.debug(LTag.NSCLIENT, "Received Food Data: $data")

        try {
            val foods = mutableListOf<FD>()
            if (data is List<*>) {
                for (i in 0 until data.size)
                    foods += (data[i] as NSFood).toFood()
            }
            storeDataForDb.addToFoods(foods)
        } catch (error: Exception) {
            aapsLogger.error("Error: ", error)
            nsClientRepository.addLog("◄ ERROR", error.message)
        }
    }

    /**
     * Take over a profile store from Nightscout.
     *
     * A **paired client** does not: its profiles come from the master over the client-control sync
     * channel, and accepting the Nightscout copy as well would give two writers for one list, fighting
     * through [LongNonKey.LocalProfileLastChange]. An **unpaired client** has no other source and needs
     * profiles to display and calculate with, so it keeps accepting them (read-only — it cannot edit).
     * A **master** is unchanged: the user's `ns_receive_profile_store` setting decides.
     */
    suspend fun processProfile(profileJson: JsonObject, doFullSync: Boolean) {
        val accept =
            if (config.AAPSCLIENT) !nsClient.masterOrPairedClientFlow.value
            else preferences.get(BooleanKey.NsClientAcceptProfileStore) || doFullSync
        if (accept) {
            val store = profileStoreProvider().with(profileJson)
            val createdAt = store.getStartDate()
            val lastLocalChange = preferences.get(LongNonKey.LocalProfileLastChange)
            aapsLogger.debug(LTag.PROFILE, "Received profileStore: createdAt: $createdAt Local last modification: $lastLocalChange")
            if (createdAt > lastLocalChange || createdAt % 1000 == 0L) { // whole second means edited in NS
                // Only acknowledge the profile to NS after a successful import. If the import
                // fails (storage exception), leave NS believing the profile wasn't synced so it
                // re-pushes on the next sync round.
                val result = profileRepository.loadFromNs(store)
                if (result.isSuccess) {
                    nsClient.dataSyncSelector.profileReceived(store.getStartDate())
                } else {
                    aapsLogger.error(LTag.PROFILE, "loadFromNs failed; not acknowledging profile to NS", result.exceptionOrNull())
                }
                aapsLogger.debug(LTag.PROFILE, "Received profileStore: $profileJson")
            }
        }
    }
}
