package dev.jokr.localnet;

import android.os.Bundle;

import java.util.HashMap;

import dev.jokr.localnet.models.ConnectedClients;
import dev.jokr.localnet.models.Payload;
import dev.jokr.localnet.models.SessionMessage;

/**
 * Created by JoKr on 8/30/2016.
 */
public abstract class LocalSession {

    /*
     *  Class that represent session of messaging between devices.
     *  All session events (like sending or receiving a message) and logic connected to them
     *  should be implemented here, not in Activity class or others.
     *
     *  Code here will be executed in service.
     *
     */

    private Communicator communicator;

    public LocalSession() {}

    public void sendMessage(long recipientId, Payload<?> payload) {
        communicator.sendMessage(recipientId, payload);
    }

    public void sendBroadcastMessage(Payload<?> payload) {
        communicator.sendBroadcastMessage(payload);
    }

    public void sendUiEvent(Payload<?> payload) {
        communicator.sendUiEvent(payload);
    }

    public void preCreateInit(Communicator communicator) {
        this.communicator = communicator;
    }

    public abstract void onCreate(Bundle bundle, ConnectedClients connectedClients);
    public abstract void onReceiveMessage(long recipientId, Payload payload);
    public abstract void onEvent(Payload<?> payload);

}
