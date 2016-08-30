package dev.jokr.localnet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import dev.jokr.localnet.discovery.DiscoveryThread;
import dev.jokr.localnet.discovery.models.DiscoveryReply;
import dev.jokr.localnet.session._ServerSocketService;

/**
 * Created by JoKr on 8/27/2016.
 */
public class _LocalServer {

    public static final String ACTION_INIT = "init";
    public static final String ACTION_MESSAGE = "message";

    private Context context;
    private InitReceiver receiver;

    public _LocalServer(Context context) {
        this.context = context;
        registerInitBroadcastReceiver();
    }

    public void init() {
        Intent i = new Intent(context, _ServerSocketService.class);
        i.putExtra("key", "yolo");
        Log.d("USER", "starting service");
        context.startService(i);

    }

    private void registerInitBroadcastReceiver() {
        receiver = new InitReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_INIT);
//        context.registerReceiver(receiver, intentFilter);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);
    }

    private class InitReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            DiscoveryReply discoveryReply = (DiscoveryReply) intent.getExtras().getSerializable(DiscoveryReply.class.getName());
            Log.d("USER", "Received: " + discoveryReply.getIp() + ":" + discoveryReply.getPort());
            DiscoveryThread thread = new DiscoveryThread(discoveryReply);
            new Thread(thread).start();
        }
    }


}
