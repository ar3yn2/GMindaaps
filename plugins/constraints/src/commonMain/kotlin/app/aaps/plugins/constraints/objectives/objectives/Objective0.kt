package app.gmaaps.plugins.constraints.objectives.objectives

import app.gmaaps.core.interfaces.aps.Loop
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.iob.IobCobCalculator
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.plugin.PluginBase
import app.gmaaps.core.interfaces.protection.PasswordCheck
import app.gmaaps.core.interfaces.pump.VirtualPump
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.sync.Tidepool
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.keys.BooleanKey
import app.gmaaps.core.keys.BooleanNonKey
import app.gmaaps.core.keys.StringKey
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.ui.CoreUiStrings
import app.gmaaps.plugins.constraints.ConstraintsStrings
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.IntKey
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.binding

// Contributed rather than listed in a graph. ObjectivesPlugin takes List<Objective> and is itself
// contributed to AppScope, so the objectives have to be reachable from the same graph.
@ContributesIntoMap(AppScope::class, binding = binding<Objective>())
@IntKey(0)
@SingleIn(AppScope::class)
@Inject
class Objective0(
    preferences: Preferences,
    rh: TextResolver,
    durationText: DurationText,
    dateUtil: DateUtil,
    private val activePlugin: ActivePlugin,
    private val virtualPumpPlugin: VirtualPump,
    private val persistenceLayer: PersistenceLayer,
    private val loop: Loop,
    private val iobCobCalculator: IobCobCalculator,
    private val passwordCheck: PasswordCheck,
) : Objective(preferences, rh, dateUtil, durationText, "config", ConstraintsStrings.objectives_0_objective, ConstraintsStrings.objectives_0_gate) {

    val tidepoolPlugin get() = activePlugin.getSpecificPluginsListByInterface(Tidepool::class).firstOrNull() as Tidepool?

    init {
        tasks.add(object : Task(this, ConstraintsStrings.objectives_bgavailableinns) {
            override suspend fun isCompleted(): Boolean {
                return preferences.get(BooleanNonKey.ObjectivesBgIsAvailableInNs) || tidepoolPlugin?.hasWritePermission == true
            }
        })
        tasks.add(object : Task(this, ConstraintsStrings.synchaswritepermission) {
            override suspend fun isCompleted(): Boolean {
                return activePlugin.firstActiveSync?.hasWritePermission == true || tidepoolPlugin?.hasWritePermission == true
            }
        })
        tasks.add(object : Task(this, CoreUiStrings.virtualpump_uploadstatus_title) {
            override suspend fun isCompleted(): Boolean {
                return preferences.get(BooleanKey.VirtualPumpStatusUpload) || tidepoolPlugin?.hasWritePermission == true
            }

            override fun shouldBeIgnored(): Boolean {
                return !(virtualPumpPlugin as PluginBase).isEnabled()
            }
        })
        tasks.add(
            object : Task(this, ConstraintsStrings.objectives_pumpstatusavailableinns) {
                override suspend fun isCompleted(): Boolean {
                    return preferences.get(BooleanNonKey.ObjectivesPumpStatusIsAvailableInNS) || tidepoolPlugin?.hasWritePermission == true
                }
            }.learned(Learned(ConstraintsStrings.objectives_0_learned))
        )
        tasks.add(object : Task(this, ConstraintsStrings.hasbgdata) {
            override suspend fun isCompleted(): Boolean {
                return iobCobCalculator.ads.lastBg() != null
            }
        })
        tasks.add(object : Task(this, ConstraintsStrings.loopenabled) {
            override suspend fun isCompleted(): Boolean {
                return (loop as PluginBase).isEnabled()
            }
        })
        tasks.add(object : Task(this, ConstraintsStrings.apsselected) {
            override suspend fun isCompleted(): Boolean {
                val usedAPS = activePlugin.activeAPS ?: return false
                return (usedAPS as PluginBase).isEnabled()
            }
        })
        tasks.add(object : Task(this, CoreUiStrings.activate_profile) {
            override suspend fun isCompleted(): Boolean = persistenceLayer.getEffectiveProfileSwitchActiveAt(dateUtil.now()) != null
        })
        tasks.add(
            UITask(this, ConstraintsStrings.verify_master_password, "master_password") { task, callback, showMessage ->
                if (preferences.get(StringKey.ProtectionMasterPassword) == "") {
                    showMessage(rh.gs(CoreUiStrings.master_password_not_set))
                } else {
                    passwordCheck.queryPassword(
                        StringKey.ProtectionMasterPassword.title, StringKey.ProtectionMasterPassword,
                        ok = {
                            task.answered = true
                            callback.run()
                        })
                }
            }
        )
    }
}
