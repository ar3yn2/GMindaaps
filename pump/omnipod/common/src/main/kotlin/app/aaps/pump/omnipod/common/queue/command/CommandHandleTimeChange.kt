package app.gmaaps.pump.omnipod.common.queue.command

import app.gmaaps.core.interfaces.queue.CustomCommand

class CommandHandleTimeChange(val requestedByUser: Boolean) : CustomCommand {

    override val statusDescription = "HANDLE TIME CHANGE"
}

