package dev.jokr.localnetworkapp;

import android.os.Bundle;
import android.util.Log;

import dev.jokr.localnet.LocalSession;
import dev.jokr.localnet.models.ConnectedClients;
import dev.jokr.localnet.models.Payload;
import dev.jokr.localnet.models.SessionMessage;

/**
 * Created by JoKr on 8/31/2016.
 */
public class GameSession extends LocalSession{

    @Override
    public void onCreate(Bundle bundle, ConnectedClients connectedClients){
        Log.d("USER", "SESSION: onCreate");
        long id = (long) connectedClients.getAllClientsIds().toArray()[0];
        sendMessage(id, new Payload<Integer>(155));
    }

    @Override
    public void onReceiveMessage(long recipientId, Payload payload) {
        Log.d("USER", "SESSION: onReceiveMessage" + payload.getPayload());
        sendUiEvent(payload);
    }

    @Override
    public void onEvent(Payload<?> payload) {
        sendBroadcastMessage(payload);
    }
}
