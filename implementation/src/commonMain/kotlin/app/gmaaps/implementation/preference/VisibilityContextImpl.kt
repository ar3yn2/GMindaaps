package app.gmaaps.implementation.preference

import app.gmaaps.core.data.model.GV
import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.constraints.ConstraintsChecker
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.db.observeChanges
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import app.gmaaps.core.interfaces.rx.collectResilient
import app.gmaaps.core.interfaces.sync.NsClient
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.keys.interfaces.VisibilityContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlin.concurrent.Volatile

/**
 * Implementation of [VisibilityContext] that provides runtime context
 * for evaluating preference visibility conditions.
 *
 * This class bridges the gap between the preference key definitions (which declare
 * visibility conditions) and the runtime state of the app (pump type, BG source, etc.).
 */
@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class VisibilityContextImpl(
    private val activePlugin: ActivePlugin,
    private val persistenceLayer: PersistenceLayer,
    private val constraintsChecker: ConstraintsChecker,
    private val config: Config,
    private val aapsLogger: AAPSLogger,
    private val nsClient: NsClient,
    private val appScope: CoroutineScope,
    override val preferences: Preferences
) : VisibilityContext {

    override val isClient: Boolean
        get() = config.AAPSCLIENT

    // Live snapshot of the stable Client-Control pairing signal (always true on a master). Read per
    // isVisible() evaluation so CLIENT_PAIRED-gated elements appear/disappear the moment pairing flips.
    override val masterOrPairedClient: Boolean
        get() = nsClient.masterOrPairedClientFlow.value

    // Cached off-main: advancedFilteringSupported is read from Compose preference-screen visibility
    // predicates on the main thread, so it must not hit the DB synchronously. Seeded in init and
    // refreshed whenever glucose values change (the value derives from the latest GV's sensor).
    // Until the first async load completes it reports false (advanced filtering not supported).
    @Volatile private var cachedAdvancedFiltering: Boolean = false

    init {
        appScope.launch { cachedAdvancedFiltering = persistenceLayer.isAdvancedFilteringSupported() }
        observeAdvancedFiltering()
    }

    // Debounced so a Nightscout backfill burst (hundreds of GV inserts) collapses into a single
    // recompute — the value only depends on the latest glucose value's sensor and changes rarely.
    @OptIn(FlowPreview::class)
    private fun observeAdvancedFiltering() {
        persistenceLayer.observeChanges<GV>()
            .debounce(1000L)
            .collectResilient(appScope, aapsLogger, LTag.CORE) {
                cachedAdvancedFiltering = persistenceLayer.isAdvancedFilteringSupported()
            }
    }

    override val isPatchPump: Boolean
        get() = activePlugin.activePump.pumpDescription.isPatchPump

    override val isBatteryReplaceable: Boolean
        get() = activePlugin.activePump.pumpDescription.isBatteryReplaceable

    override val isBatteryChangeLoggingEnabled: Boolean
        get() = activePlugin.activePump.isBatteryChangeLoggingEnabled()

    override val advancedFilteringSupported: Boolean
        get() = cachedAdvancedFiltering

    override val isPumpInitialized: Boolean
        get() = activePlugin.activePump.isInitialized()

    override val isConcentrationEnabled: Boolean
        get() = constraintsChecker.isConcentrationEnabled().value()

    override val apsOffersDynamicSensitivity: Boolean
        get() = activePlugin.activeAPS?.offersDynamicSensitivity() == true
}

