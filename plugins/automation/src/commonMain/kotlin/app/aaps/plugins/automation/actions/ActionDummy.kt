package app.gmaaps.plugins.automation.actions

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.pump.PumpEnactResult
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.keys.interfaces.TextRef

// Used for instantiation of other actions only
class ActionDummy(
    aapsLogger: AAPSLogger,
    rh: TextResolver,
    pumpEnactResultProvider: () -> PumpEnactResult
) : Action(aapsLogger, rh, pumpEnactResultProvider) {

    override fun friendlyName(): TextRef {
        throw NotImplementedError("An operation is not implemented")
    }

    override fun shortDescription(): String {
        throw NotImplementedError("An operation is not implemented")
    }

    override suspend fun doAction(): PumpEnactResult {
        throw NotImplementedError("An operation is not implemented")
    }

    override fun isValid(): Boolean = false
}

