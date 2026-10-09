package app.gmaaps.ui.compose.history

import app.gmaaps.core.interfaces.iob.IobCobCalculator
import app.gmaaps.core.interfaces.overview.OverviewData
import app.gmaaps.core.interfaces.overview.graph.OverviewDataCache
import app.gmaaps.core.interfaces.workflow.CalculationSignalsEmitter

/**
 * Surface the history-browser ViewModel consumes — isolates the
 * `HistoryViewModel` (and thus everything in `:ui`) from the concrete
 * `HistoryBrowserData` wiring (which lives in `:app` because it
 * instantiates `plugins:main` types).
 */
interface HistoryScope {

    val overviewData: OverviewData
    val signals: CalculationSignalsEmitter
    val cache: OverviewDataCache
    val iobCobCalculator: IobCobCalculator

    fun onDestroy()
}

