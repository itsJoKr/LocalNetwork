package dev.jokr.localnet.discovery.models;

import java.io.Serializable;

/**
 * Created by JoKr on 8/27/2016.
 */
public class DiscoveryRequest<T> implements Serializable {

    private String name;
    private T payload;

    public DiscoveryRequest(String name, T payload) {
        this.name = name;
        this.payload = payload;
    }

    public String getName() {
        return name;
    }

    public T getPayload() {
        return payload;
    }
}
