package dev.jokr.localnet.models;

import java.io.Serializable;

/**
 * Created by JoKr on 8/29/2016.
 */
public class SessionMessage implements Serializable {

    public static final int NONE = 0;
    public static final int START = 1;
    public static final int END = 2;

    private Payload<?> payload;
    private int signal;


    public SessionMessage(Payload<?> payload) {
        this.payload = payload;
        this.signal = NONE;
    }

    public SessionMessage(Payload<?> payload, int signal) {
        this.payload = payload;
        this.signal = signal;
    }

    public int getSignal() {
        return signal;
    }

    public Payload<?> getPayload() {
        return payload;
    }
}
