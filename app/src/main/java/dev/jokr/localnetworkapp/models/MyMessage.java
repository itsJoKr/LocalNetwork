package dev.jokr.localnetworkapp.models;

import java.io.Serializable;

/**
 * Created by JoKr on 9/12/2016.
 */
public class MyMessage implements Serializable {

    /*
     * You can send any object using LocalNet library by implementing Serializable and
     * packing it into Payload<*your class*>
     * You just need to be sure when receiving Payload to cast it back to your appropriate class.
     * In this app example we are using this MyMessage class to send some data.
     */

    public String message;

    public MyMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
