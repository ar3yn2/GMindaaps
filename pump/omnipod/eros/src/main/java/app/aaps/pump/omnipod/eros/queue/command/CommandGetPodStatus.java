package app.gmaaps.pump.omnipod.eros.queue.command;

import org.jetbrains.annotations.NotNull;

import app.gmaaps.core.interfaces.queue.CustomCommand;

public final class CommandGetPodStatus implements CustomCommand {
    @NotNull @Override public String getStatusDescription() {
        return "GET POD STATUS";
    }
}

