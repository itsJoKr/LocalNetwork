package dev.jokr.localnet;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import dev.jokr.localnet.discovery.JoinThread;
import dev.jokr.localnet.discovery.models.DiscoveryReply;

/**
 * Created by JoKr on 8/28/2016.
 */
public class LocalClient implements JoinThread.ServerDiscoveryCallback {

    private Context context;

    public LocalClient(Context context) {
        this.context = context;
    }

    public void connect() {
        JoinThread thread = new JoinThread(this);
        new Thread(thread).start();
    }

    @Override
    public void serverDiscovered(DiscoveryReply reply) {
        Intent i = new Intent(context, ClientService.class);
        i.putExtra(ClientService.DISCOVERY_REPLY, reply);
        context.startService(i);
    }

    @Override
    public void serverDiscoveryTimeout() {

    }
}
