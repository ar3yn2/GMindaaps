package app.gmaaps.implementation.iob

import app.gmaaps.core.interfaces.aps.GlucoseStatus
import app.gmaaps.core.interfaces.iob.GlucoseStatusProvider
import app.gmaaps.core.interfaces.plugin.ActivePlugin
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class GlucoseStatusProviderImpl(
    private val activePlugin: ActivePlugin
) : GlucoseStatusProvider {

    override val glucoseStatusData: GlucoseStatus?
        get() = getGlucoseStatusData()

    override fun getGlucoseStatusData(allowOldData: Boolean): GlucoseStatus? =
        activePlugin.activeAPS?.getGlucoseStatusData(allowOldData)
}
