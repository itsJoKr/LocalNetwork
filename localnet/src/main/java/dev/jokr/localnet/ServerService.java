package dev.jokr.localnet;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.text.format.Formatter;
import android.util.Log;

import java.util.HashMap;

import dev.jokr.localnet.discovery.models.DiscoveryReply;
import dev.jokr.localnet.models.ConnectedClients;
import dev.jokr.localnet.models.Payload;
import dev.jokr.localnet.models.RegisterMessage;
import dev.jokr.localnet.models.SessionMessage;
import dev.jokr.localnet.utils.NetworkUtil;

/**
 * Created by JoKr on 8/28/2016.
 */
public class ServerService extends Service implements ServerSocketThread.ServiceCallback, Communicator {

    public static final String ACTION = "action";
    public static final int NOTIFICATION_ID = 521;

    // Keys for extras
    public static final String CLASS = "class";
    public static final String BUNDLE = "bundle";
    public static final String PAYLOAD = "payload";

    // Possible service actions:
    public static final int START_SESSION = 1;
    public static final int SESSION_EVENT = 2;


    private HashMap<Long, RegisterMessage> registeredClients;
    private LocalSession session;
    private LocalBroadcastManager manager;
    private Thread serverSocketThread;
    private Thread discoverySocketThread;

    @Override
    public void onCreate() {
        super.onCreate();

        this.manager = LocalBroadcastManager.getInstance(this);
        registeredClients = new HashMap<>();

        serverSocketThread = new Thread(new ServerSocketThread(this));
        serverSocketThread.start();

        runServiceInForeground();
    }

    /*
        Multiple calls to Context.startService() will result in multiple calls to onStartCommand,
        but only one call to onCreate, so we use it as a way to send data to Service.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("USER", "onStartCommand called");

        int action = intent.getIntExtra(ACTION, 0);
        processAction(action, intent);


        return START_NOT_STICKY;
    }

    private void processAction(int action, Intent intent) {
        if (action == 0)
            return;

        if (action == START_SESSION)
            startSession((Class) intent.getSerializableExtra(CLASS), intent.getBundleExtra(BUNDLE));
        else if (action == SESSION_EVENT)
            session.onEvent((Payload<?>) intent.getSerializableExtra(PAYLOAD));
    }

    private void startSession(Class c, Bundle b) {
        try {
            Object o = c.newInstance();
            if (!LocalSession.class.isInstance(o)) {
                throw new IllegalArgumentException("Class " + c.getName() + " is not instance of LocalSession");
            }
            session = (LocalSession) o;
            session.preCreateInit(this);
            session.onCreate(b, new ConnectedClients(registeredClients));
            sendSessionStartMessage();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void sendSessionStartMessage() {
        SessionMessage message = new SessionMessage(null, SessionMessage.START);
        for (RegisterMessage client : registeredClients.values()) {
            Thread t = new Thread(new SendHandler(message, client.getIp(), client.getPort()));
            t.start();
        }
    }


    private void runServiceInForeground() {
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("LocalNet Session")
                .setContentText("Session is currently running")
                .setSmallIcon(R.drawable.ic_play_circle_filled_black_24dp)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onInitializedSocket(int port) {
        discoverySocketThread = new Thread(new DiscoverySocketThread(new DiscoveryReply(getLocalIp(), port)));
        discoverySocketThread.start();
    }

    @Override
    public void onClientConnected(RegisterMessage message) {
        Log.d("USER", "onClientConnected: " + message.getPayload());
        Long id = NetworkUtil.getIdFromIpAddress(message.getIp());
        registeredClients.put(id, message);

        Intent i = new Intent(LocalServer.CONNECTED_CLIENT);
        i.putExtra(RegisterMessage.class.getName(), message);
        manager.sendBroadcast(i);
    }

    @Override
    public void onSessionMessage(SessionMessage message, String senderIp) {
        if (session != null) {
            session.onReceiveMessage(NetworkUtil.getIdFromIpAddress(senderIp), message.getPayload());
        }
    }


    private String getLocalIp() {
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }

    @Override
    public void sendMessage(long recipientId, Payload payload) {
        SessionMessage message = new SessionMessage(payload);
        RegisterMessage client = null;
        if (registeredClients.containsKey(recipientId)) {
            client = registeredClients.get(recipientId);
        } else {
            throw new IllegalArgumentException("Recipient id " + recipientId + " is not in registered clients list");
        }
        Thread t = new Thread(new SendHandler(message, client.getIp(), client.getPort()));
        t.start();
    }

    @Override
    public void sendBroadcastMessage(Payload<?> payload) {
        SessionMessage message = new SessionMessage(payload);
        for (RegisterMessage client : registeredClients.values()) {
            Thread t = new Thread(new SendHandler(message, client.getIp(), client.getPort()));
            t.start();
        }
    }

    @Override
    public void sendUiEvent(Payload<?> payload) {
        Intent i = new Intent(LocalServer.UI_EVENT);
        SessionMessage sessionMessage = new SessionMessage(payload);
        i.putExtra(SessionMessage.class.getName(), sessionMessage);
        manager.sendBroadcast(i);
    }

}
