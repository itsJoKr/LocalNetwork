package dev.jokr.localnet.models;

import java.io.Serializable;

/**
 * Created by JoKr on 8/29/2016.
 */
public class SessionMessage<T> implements Serializable {
    private T payload;

    public SessionMessage(T payload) {
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }
}
