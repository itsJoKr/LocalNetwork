package dev.jokr.localnet.models;

import java.io.Serializable;

/**
 * Created by JoKr on 8/29/2016.
 */
public class SessionMessage implements Serializable {
    private Payload<?> payload;

    public SessionMessage(Payload<?> payload) {
        this.payload = payload;
    }

    public Payload<?> getPayload() {
        return payload;
    }
}
