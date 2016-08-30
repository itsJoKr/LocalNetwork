package dev.jokr.localnet;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.jokr.localnet.discovery.models.DiscoveryReply;
import dev.jokr.localnet.models.RegisterMessage;
import dev.jokr.localnet.utils.NetworkUtil;

/**
 * Created by JoKr on 8/28/2016.
 */
public class ServerService extends Service implements ServerSocketHandler.ServiceCallback {

    private ServerSocketHandler serverSocketHandler;
    private DiscoverySocketHandler discoverySocketHandler;

//    private List<RegisterMessage<?>> registerClients;
    private HashMap<Long, RegisterMessage<?>> registeredClients;

    @Override
    public void onCreate() {
        super.onCreate();
        registeredClients = new HashMap<>();
        Thread t = new Thread(new ServerSocketHandler(this));
        t.start();
    }

    /*
        Multiple calls to Context.startService() will result in multiple calls to onStartCommand,
        but only one call to onCreate, so we use it as a way to send data to Service.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("USER", "onStartCommand called");

//        Message msg = serverSocketHandler.obtainMessage();
//        Bundle bundle = new Bundle();
//        bundle.putString("key", message);
//        msg.setData(bundle);
//        serverSocketHandler.handleMessage(msg);

        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onInitializedSocket(int port) {
        // Create discovery socket handler
        Log.d("USER", "onInitializedSocket, starting Handler...");
//        HandlerThread thread = new HandlerThread("discoveryHandler", Process.THREAD_PRIORITY_FOREGROUND);
//        thread.start();
//        Looper serviceLooper = thread.getLooper();
//        discoverySocketHandler = new DiscoverySocketHandler(serviceLooper, new DiscoveryReply(getLocalIp(), port));
        Thread t = new Thread(new DiscoverySocketHandler(new DiscoveryReply(getLocalIp(), port)));
        t.start();
    }

    @Override
    public void onClientConnected(RegisterMessage<?> message) {
        // Send message to object using local broadcast manager
        Log.d("USER", "onClientConnected: " + message.getPayload());
        Long id = NetworkUtil.getIdFromIpAddress(message.getIp());
        registeredClients.put(id, message);
    }

    private String getLocalIp() {
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }
}
