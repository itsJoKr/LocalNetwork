package dev.jokr.localnet.models;

import java.io.Serializable;

/**
 * Created by JoKr on 8/28/2016.
 */
public class IncomingServerMessage implements Serializable {

    private String type;
    private Object message;

    public IncomingServerMessage(String type, Object message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public Object getMessage() {
        return message;
    }
}
