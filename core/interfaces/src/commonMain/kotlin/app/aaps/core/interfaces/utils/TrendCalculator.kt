package app.gmaaps.core.interfaces.utils

import app.gmaaps.core.data.model.TrendArrow
import app.gmaaps.core.interfaces.aps.AutosensDataStore

/**
 *  Convert BG direction value to trend arrow or calculate it if not provided
 *  If calculation is necessary more values are loaded from DB
 */
interface TrendCalculator {

    /**
     * Provide or calculate trend from newest bucketed data
     *
     * @param autosensDataStore bucketed data
     * @return TrendArrow
     */
    fun getTrendArrow(autosensDataStore: AutosensDataStore): TrendArrow?

    /**
     * Provide or calculate trend from newest bucketed data
     *
     * @param autosensDataStore bucketed data
     * @return string description of TrendArrow
     */
    fun getTrendDescription(autosensDataStore: AutosensDataStore): String
}
