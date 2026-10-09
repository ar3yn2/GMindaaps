package app.gmaaps.pump.omnipod.eros.driver.exception;

public class NonceResyncException extends OmnipodException {
    public NonceResyncException() {
        super("Nonce resync failed", true);
    }
}

