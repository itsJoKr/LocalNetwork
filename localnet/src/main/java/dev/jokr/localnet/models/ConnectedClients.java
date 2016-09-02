package dev.jokr.localnet.models;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by JoKr on 8/30/2016.
 */
public class ConnectedClients {

    private HashMap<Long, RegisterMessage> registeredClients;

    public ConnectedClients(HashMap<Long, RegisterMessage> registeredClients) {
        this.registeredClients = registeredClients;
    }

    public Payload<?> getPayload(int clientId) {
        if (registeredClients.containsKey(clientId))
            return registeredClients.get(clientId).getPayload();
        else
            return null;
    }

    public Set<Long> getAllClientsIds() {
        return registeredClients.keySet();
    }

    public int getClientsSize() {
        return registeredClients.size();
    }

}
