package app.gmaaps.di.metro

import app.gmaaps.core.interfaces.constraints.ConstraintsChecker
import app.gmaaps.core.interfaces.db.ProcessedTbrEbData
import app.gmaaps.core.interfaces.insulin.ConcentrationHelper
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.profile.ProfileFunction
import app.gmaaps.core.interfaces.profile.ProfileUtil
import app.gmaaps.core.interfaces.resources.ResourceHelper
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.interfaces.utils.DecimalFormatter
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.plugins.aps.logger.LoggerCallback
import app.gmaaps.plugins.aps.openAPS.APSResultObject
import app.gmaaps.plugins.aps.openAPSAMA.DetermineBasalAdapterAMAJS
import app.gmaaps.plugins.aps.openAPSAMA.DetermineBasalResultAMAFromJS
import app.gmaaps.plugins.aps.openAPSSMB.DetermineBasalAdapterSMBJS
import app.gmaaps.plugins.aps.openAPSSMB.DetermineBasalResultSMBFromJS
import app.gmaaps.plugins.aps.openAPSSMBAutoISF.DetermineBasalAdapterAutoISFJS
import app.gmaaps.plugins.aps.openAPSSMBDynamicISF.DetermineBasalAdapterSMBDynamicISFJS
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ClassKey
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.MembersInjector
import dev.zacsweers.metro.Provides
import kotlin.reflect.KClass

/**
 * Scope of [AlgTestGraph]. Its own, deliberately.
 * `AppScope` would make this graph aggregate every `@ContributesTo(AppScope::class)` container in the
 * tree and try to build the whole application a second time. A private scope keeps it to the handful of
 * classes below.
 */
abstract class AlgTestScope private constructor()

/**
 * The app-wide objects the reference algorithm helpers need, handed in rather than built.
 */
@BindingContainer
class AlgTestLeaves(
    private val aapsLogger: AAPSLogger,
    private val constraintChecker: ConstraintsChecker,
    private val preferences: Preferences,
    private val activePlugin: ActivePlugin,
    private val processedTbrEbData: ProcessedTbrEbData,
    private val profileFunction: ProfileFunction,
    private val rh: ResourceHelper,
    private val decimalFormatter: DecimalFormatter,
    private val ch: ConcentrationHelper,
    private val dateUtil: DateUtil,
    private val profileUtil: ProfileUtil
) {

    @Provides fun aapsLogger(): AAPSLogger = aapsLogger
    @Provides fun constraintChecker(): ConstraintsChecker = constraintChecker
    @Provides fun preferences(): Preferences = preferences
    @Provides fun activePlugin(): ActivePlugin = activePlugin
    @Provides fun processedTbrEbData(): ProcessedTbrEbData = processedTbrEbData
    @Provides fun profileFunction(): ProfileFunction = profileFunction
    @Provides fun rh(): ResourceHelper = rh
    @Provides fun decimalFormatter(): DecimalFormatter = decimalFormatter
    @Provides fun ch(): ConcentrationHelper = ch
    @Provides fun dateUtil(): DateUtil = dateUtil
    @Provides fun profileUtil(): ProfileUtil = profileUtil
}

/**
 * Member injectors for the reference algorithm helpers, replacing `AlgModule`'s
 * `@ContributesAndroidInjector` entries.
 * The lookup is by **runtime** class, so the two `APSResultObject` subclasses need their own entries -
 * the base class is injected from its own `init`, where `this` is already the subclass. `APSResultObject`
 * is here too because a bare one is created when a result copies itself.
 */
@ContributesTo(AlgTestScope::class)
@BindingContainer
object AlgMemberInjectors {

    @Provides @IntoMap @ClassKey(LoggerCallback::class)
    fun bindLoggerCallback(injector: MembersInjector<LoggerCallback>): MembersInjector<*> = injector

    @Provides @IntoMap @ClassKey(APSResultObject::class)
    fun bindAPSResultObject(injector: MembersInjector<APSResultObject>): MembersInjector<*> = injector

    @Provides @IntoMap @ClassKey(DetermineBasalResultAMAFromJS::class)
    fun bindDetermineBasalResultAMA(
        injector: MembersInjector<DetermineBasalResultAMAFromJS>
    ): MembersInjector<*> = injector

    @Provides @IntoMap @ClassKey(DetermineBasalResultSMBFromJS::class)
    fun bindDetermineBasalResultSMB(
        injector: MembersInjector<DetermineBasalResultSMBFromJS>
    ): MembersInjector<*> = injector

    @Provides @IntoMap @ClassKey(DetermineBasalAdapterAMAJS::class)
    fun bindDetermineBasalAdapterAMA(
        injector: MembersInjector<DetermineBasalAdapterAMAJS>
    ): MembersInjector<*> = injector

    @Provides @IntoMap @ClassKey(DetermineBasalAdapterSMBJS::class)
    fun bindDetermineBasalAdapterSMB(
        injector: MembersInjector<DetermineBasalAdapterSMBJS>
    ): MembersInjector<*> = injector

    @Provides @IntoMap @ClassKey(DetermineBasalAdapterSMBDynamicISFJS::class)
    fun bindDetermineBasalAdapterDynamicISF(
        injector: MembersInjector<DetermineBasalAdapterSMBDynamicISFJS>
    ): MembersInjector<*> = injector

    @Provides @IntoMap @ClassKey(DetermineBasalAdapterAutoISFJS::class)
    fun bindDetermineBasalAdapterAutoISF(
        injector: MembersInjector<DetermineBasalAdapterAutoISFJS>
    ): MembersInjector<*> = injector
}

/**
 * Metro graph for the reference algorithm helpers in `androidTest`.
 */
@DependencyGraph(AlgTestScope::class)
interface AlgTestGraph {

    val memberInjectors: Map<KClass<*>, MembersInjector<*>>

    @DependencyGraph.Factory
    interface Factory {

        fun create(@Includes leaves: AlgTestLeaves): AlgTestGraph
    }
}

