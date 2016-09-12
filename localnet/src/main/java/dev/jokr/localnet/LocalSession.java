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
     *
     *  Code here will be executed in service.
     *
     */

    // Communicator for interacting with running service
    private Communicator communicator;

    public LocalSession() {}

    /*
     * This method sends session message to specified recipient. Recipient will
     * receive message in their LocalClient instance.
     */
    public void sendMessage(long recipientId, Payload<?> payload) {
        communicator.sendMessage(recipientId, payload);
    }

    /*
     * This method is used to send message to all clients connected.
     */
    public void sendBroadcastMessage(Payload<?> payload) {
        communicator.sendBroadcastMessage(payload);
    }

    /*
     * Logic in this class is executed in service. If you want show some data on the UI, you can use
     * this method to send data to your Activity/Fragment or other classes.
     */
    public void sendUiEvent(Payload<?> payload) {
        communicator.sendUiEvent(payload);
    }

    /*
     * This method is used for setting communicator with running service. Called before onCreate.
     */
    public void preCreateInit(Communicator communicator) {
        this.communicator = communicator;
    }

    /*
     * This is where you should start your session logic. You receive bundle (if you set any from
     * Local Server) and connected clients.
     */
    public abstract void onCreate(Bundle bundle, ConnectedClients connectedClients);

    /*
     *  In this method you will receive message sent from clients.
     */
    public abstract void onReceiveMessage(long recipientId, Payload payload);

    /*
     *  You can send events from your UI (activities, fragments...) to LocalSession at any time.
     *  You will receive data in this method.
     */
    public abstract void onEvent(Payload<?> payload);

}
