package dev.jokr.localnet;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.Formatter;
import android.util.Log;

import java.io.Serializable;

import dev.jokr.localnet.discovery.JoinThread;
import dev.jokr.localnet.discovery.models.DiscoveryReply;
import dev.jokr.localnet.models.IncomingServerMessage;
import dev.jokr.localnet.models.Payload;
import dev.jokr.localnet.models.RegisterMessage;
import dev.jokr.localnet.models.SessionMessage;
import dev.jokr.localnet.utils.MessageType;

/**
 * Created by JoKr on 8/29/2016.
 */
public class ClientService  extends Service implements ClientSocketHandler.ServiceCallback, JoinThread.ServerDiscoveryCallback {
//    public static final String REQUEST_BUNDLE = "request_bundle";
    public static final String DISCOVERY_REPLY = "reply";

    private ClientSocketHandler clientSocketHandler;
    private SendHandler sendHandler;

    private DiscoveryReply reply;
    private LocalBroadcastManager manager;

    @Override
    public void onCreate() {
        super.onCreate();

        this.manager = LocalBroadcastManager.getInstance(this);
        Thread t = new Thread(new ClientSocketHandler(this));
        t.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        DiscoveryRequest<?> discoveryRequest = (DiscoveryRequest<?>) intent.getSerializableExtra(SendHandler.MESSAGE);
//        String address = intent.getStringExtra(SendHandler.ADDRESS);
//        int port = intent.getIntExtra(SendHandler.PORT, 0);
//        Message message = sendHandler.obtainMessage();
//        Bundle bundle = intent.getBundleExtra(REQUEST_BUNDLE);
//        message.setData(bundle);
//        sendHandler.handleMessage(message);

        DiscoveryReply reply = (DiscoveryReply) intent.getSerializableExtra(DISCOVERY_REPLY);
        if (reply != null) {
            this.reply = reply;
        }

        // Todo: sending messages
//        Thread t = new Thread(new SendHandler(bundle));
//        t.start();

        return START_STICKY;
    }

    @Override
    public void onInitializedSocket(int port) {
        RegisterMessage message = new RegisterMessage(new Payload<Integer>(42), getLocalIp(), port);
        Thread t = new Thread(new SendHandler(new IncomingServerMessage(MessageType.REGISTER, message), reply.getIp(), reply.getPort()));
        t.start();
    }

    @Override
    public void onSessionMessage(SessionMessage message) {
        Intent i = new Intent(LocalClient.SESSION_MESSAGE);
        i.putExtra(SessionMessage.class.getName(), message);
        manager.sendBroadcast(i);
    }

    @Override
    public void serverDiscovered(DiscoveryReply reply) {
        Log.d("USER", "SERVER DISCOVERED!");
        Thread t = new Thread(new ClientSocketHandler(this));
        t.start();
    }

    @Override
    public void serverDiscoveryTimeout() {
        Log.d("USER", "server timeout!");
    }

    private String getLocalIp() {
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }
}
