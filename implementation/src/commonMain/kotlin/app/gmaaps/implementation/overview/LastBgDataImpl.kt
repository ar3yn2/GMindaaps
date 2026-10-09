package app.gmaaps.implementation.overview

import app.gmaaps.core.data.configuration.Constants
import app.gmaaps.core.data.iob.InMemoryGlucoseValue
import app.gmaaps.core.data.time.T
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.iob.IobCobCalculator
import app.gmaaps.core.interfaces.overview.LastBgData
import app.gmaaps.core.interfaces.profile.ProfileFunction
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.keys.UnitDoubleKey
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.objects.extensions.fromGv
import app.gmaaps.core.objects.extensions.valueToUnits
import app.gmaaps.core.ui.CoreUiStrings
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.runBlocking

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class LastBgDataImpl(
    private val rh: TextResolver,
    private val dateUtil: DateUtil,
    private val persistenceLayer: PersistenceLayer,
    private val profileFunction: ProfileFunction,
    private val preferences: Preferences,
    private val iobCobCalculator: IobCobCalculator
) : LastBgData {

    override fun lastBg(): InMemoryGlucoseValue? =
        iobCobCalculator.ads.bucketedData?.firstOrNull()
            ?: runBlocking { persistenceLayer.getLastGlucoseValue() }?.let { InMemoryGlucoseValue.fromGv(it) }

    override fun isLow(): Boolean =
        lastBg()?.let { lastBg ->
            lastBg.valueToUnits(profileFunction.getUnits()) < preferences.get(UnitDoubleKey.OverviewLowMark)
        } == true

    override fun isHigh(): Boolean =
        lastBg()?.let { lastBg ->
            lastBg.valueToUnits(profileFunction.getUnits()) > preferences.get(UnitDoubleKey.OverviewHighMark)
        } == true

    override fun lastBgDescription(): String =
        when {
            isLow()  -> rh.gs(CoreUiStrings.a11y_low)
            isHigh() -> rh.gs(CoreUiStrings.a11y_high)
            else     -> rh.gs(CoreUiStrings.a11y_inrange)
        }

    override fun isActualBg(): Boolean =
        lastBg()?.let { lastBg ->
            lastBg.timestamp > dateUtil.now() - T.mins(Constants.OLD_BG_MINUTES).msecs()
        } == true
}
