package app.gmaaps.pump.omnipod.common.queue.command

import app.gmaaps.core.interfaces.queue.CustomCommand
import app.gmaaps.core.interfaces.resources.ResourceHelper
import app.gmaaps.pump.omnipod.common.R

class CommandDisableSuspendAlerts(private val rh: ResourceHelper) : CustomCommand {

    override val statusDescription: String
        get() = rh.gs(R.string.omnipod_common_disable_suspend_alerts)
}

