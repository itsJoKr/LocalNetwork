package dev.jokr.localnet.discovery.models;

/**
 * Created by JoKr on 8/27/2016.
 */
public class DiscoveryRequest<T> {

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
