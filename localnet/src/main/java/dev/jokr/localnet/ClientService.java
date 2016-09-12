package dev.jokr.localnet;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.Formatter;

import dev.jokr.localnet.discovery.models.DiscoveryReply;
import dev.jokr.localnet.models.IncomingServerMessage;
import dev.jokr.localnet.models.Payload;
import dev.jokr.localnet.models.RegisterMessage;
import dev.jokr.localnet.models.SessionMessage;
import dev.jokr.localnet.utils.MessageType;

/**
 * Created by JoKr on 8/29/2016.
 */
public class ClientService extends Service implements ClientSocketThread.ServiceCallback {

    public static final String ACTION = "action";
    public static final String DISCOVERY_REPLY = "reply";

    // Keys for extras
    public static final String PAYLOAD = "payload";

    // Possible service actions:
    public static final int SESSION_MESSAGE = 2;
    public static final int STOP = 3;

    private ClientSocketThread clientSocketThread;
    private SendHandler sendHandler;

    private DiscoveryReply reply;
    private LocalBroadcastManager manager;

    @Override
    public void onCreate() {
        super.onCreate();

        this.manager = LocalBroadcastManager.getInstance(this);
        Thread t = new Thread(new ClientSocketThread(this));
        t.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        DiscoveryReply reply = (DiscoveryReply) intent.getSerializableExtra(DISCOVERY_REPLY);
        if (reply != null) {
            this.reply = reply;
        }

        int action = intent.getIntExtra(ACTION, 0);
        processAction(action, intent);

        return START_STICKY;
    }

    private void processAction(int action, Intent intent) {
        if (action == 0)
            return;

        if (action == SESSION_MESSAGE)
            sendSessionMessage((Payload<?>) intent.getSerializableExtra(PAYLOAD));
        else if (action == STOP)
            this.stopSelf();
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

    private void sendSessionMessage(Payload<?> payload){
        SessionMessage message = new SessionMessage(payload);
        Thread t = new Thread(new SendHandler(new IncomingServerMessage(MessageType.SESSION, message), reply.getIp(), reply.getPort()));
        t.start();
    }

    private String getLocalIp() {
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }
}
