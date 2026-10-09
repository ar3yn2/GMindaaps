package app.gmaaps.pump.omnipod.eros.driver.communication.message.response.podinfo;

import app.gmaaps.pump.omnipod.eros.driver.definition.PodInfoType;

public abstract class PodInfo {
    private final byte[] encodedData;

    PodInfo(byte[] encodedData) {
        this.encodedData = encodedData;
    }

    public abstract PodInfoType getType();

    public byte[] getEncodedData() {
        return encodedData;
    }
}

