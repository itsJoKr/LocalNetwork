package dev.jokr.localnet.models;

import java.io.Serializable;

/**
 * Created by JoKr on 8/30/2016.
 */
public class Payload<T> implements Serializable {

    private T payload;

    public Payload(T payload) {
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }


}
