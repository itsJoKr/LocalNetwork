package dev.jokr.localnet.session;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.Formatter;
import android.util.Log;

import dev.jokr.localnet.LocalServer;
import dev.jokr.localnet.discovery.models.DiscoveryReply;

public class ServerSocketService extends Service implements ServerHandler.InitCallback {
    private ServerHandler serverHandler;

    public ServerSocketService() {
    }

    public static void actionSendMessage(final Context context) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("Server", Process.THREAD_PRIORITY_FOREGROUND);
        thread.start();

        Log.d("USER", "onCreate called");

        Looper serviceLooper = thread.getLooper();
        serverHandler = new ServerHandler(serviceLooper, this);
    }

    /*
        Multiple calls to Context.startService() will result in multiple calls to onStartCommand,
        but only one call to onCreate, so we use it as a way to send data to Service.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("USER", "onStartCommand called");

        String message = intent.getExtras().getString("key");
        Log.d("USER", "message " + message);

        Message msg = serverHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("key", message);
        msg.setData(bundle);
        serverHandler.handleMessage(msg);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onInitializedSocket(int port) {
        // Send broadcast intent to client
        Intent i = new Intent(LocalServer.ACTION_INIT);
        i.putExtra(DiscoveryReply.class.getName(), new DiscoveryReply(getLocalIp(), port));
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.sendBroadcast(i);
    }

    private String getLocalIp() {
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }
}
