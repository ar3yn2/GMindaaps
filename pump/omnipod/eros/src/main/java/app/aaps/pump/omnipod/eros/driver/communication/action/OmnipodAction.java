package app.gmaaps.pump.omnipod.eros.driver.communication.action;

import app.gmaaps.pump.omnipod.eros.rileylink.manager.OmnipodRileyLinkCommunicationManager;

public interface OmnipodAction<T> {
    T execute(OmnipodRileyLinkCommunicationManager communicationService);
}

