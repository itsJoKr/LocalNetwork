package dev.jokr.localnet;

import dev.jokr.localnet.models.Payload;
import dev.jokr.localnet.models.SessionMessage;

/**
 * Created by JoKr on 8/31/2016.
 */
interface Communicator {

    public void sendMessage(long recipientId, Payload<?> payload);
    public void sendBroadcastMessage(Payload<?> payload);
    public void sendUiEvent(Payload<?> payload);
}
