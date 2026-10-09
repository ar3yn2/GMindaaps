package app.gmaaps.pump.common.driver.history

import app.gmaaps.core.interfaces.resources.ResourceHelper
import app.gmaaps.pump.common.defs.PumpHistoryEntryGroup

interface PumpHistoryEntry {

    fun prepareEntryData(resourceHelper: ResourceHelper, pumpDataConverter: PumpDataConverter)

    fun getEntryDateTime(): String

    fun getEntryType(): String

    fun getEntryValue(): String

    fun getEntryTypeGroup(): PumpHistoryEntryGroup

}
