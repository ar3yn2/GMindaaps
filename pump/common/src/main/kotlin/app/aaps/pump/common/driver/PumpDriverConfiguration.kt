package app.gmaaps.pump.common.driver

import app.gmaaps.core.data.pump.defs.PumpType
import app.gmaaps.pump.common.driver.db.PumpDriverDatabaseOperation
import app.gmaaps.pump.common.driver.history.PumpHistoryDataProvider

interface PumpDriverConfiguration {

    fun getPumpHistoryDataProvider(): PumpHistoryDataProvider?

    fun getPumpDriverDatabaseOperation(): PumpDriverDatabaseOperation

    fun getPumpType(): PumpType

    var logPrefix: String

    var canHandleDST: Boolean

    var hasService: Boolean

}

