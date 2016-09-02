package dev.jokr.localnet.models;

import java.io.Serializable;

/**
 * Created by JoKr on 8/28/2016.
 */
public class RegisterMessage implements Serializable {

    private Payload<?> payload;
    private String ip;
    private int port;

    public RegisterMessage(Payload<?> payload, String ip, int port) {
        this.payload = payload;
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public Payload<?> getPayload() {
        return payload;
    }
}
