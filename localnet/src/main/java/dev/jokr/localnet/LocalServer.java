package dev.jokr.localnet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import dev.jokr.localnet.models.Payload;
import dev.jokr.localnet.models.RegisterMessage;
import dev.jokr.localnet.models.SessionMessage;

/**
 * Created by JoKr on 8/28/2016.
 */
public class LocalServer {
    /*
     * Class for local server. You need one device to act as a server.
     * With init() server is started in discovery mode.
     * To end discovery mode and start session, call setSession(Class c). You will need to pass
     * implementation abstract class LocalSession which is main class for running session logic.
     */
    public static final String UI_EVENT = "ui_event";
    public static final String CONNECTED_CLIENT = "conn_client";

    private Context context;
    private OnUiEventReceiver receiver;

    public LocalServer(Context context) {
        this.context = context;
        registerMessageBroadcastReceiver();
    }

    /*
     * This method will start server in discovery mode. Clients can join using LocalClient.
     */
    public void init() {
        Intent i = new Intent(context, ServerService.class);
        context.startService(i);
    }

    /*
     * This method is used to end discovery mode and start session. It receives instance of
     * LocalSession class as a Class parameter.
     * LocalServer will instantiate it in service and call onCreate method in it.
     */
    public void setSession(Class c) {
        sendSessionData(c, null);
    }

    /*
     * Same as setSession, but as you can't use constructor, you can use this class to pass bundle
     * of arguments to your LocalSession implementation. You will receive this bundle in onCreate
     * in your LocalSession implementation.
     */
    public void setSession(Class c, Bundle b) {
        sendSessionData(c, b);
    }


    /*
     * This will stop service. Make sure not to call anything on this class after shutdown.
     */
    public void shutdown() {
        Intent i = new Intent(context, ServerService.class);
        i.putExtra(ServerService.ACTION, ServerService.STOP);
        context.startService(i);
    }

    /*
     * This method is used to send data to your LocalSession implementation.
     */
    public void sendLocalSessionEvent(Payload<?> payload) {
        Intent i = new Intent(context, ServerService.class);
        i.putExtra(ServerService.ACTION, ServerService.SESSION_EVENT);
        i.putExtra(ServerService.PAYLOAD, payload);
        context.startService(i);
    }

    /*
     * Set receiver if you want to receive events from LocalSession and be notified about
     * new client connections.
     */
    public void setReceiver(OnUiEventReceiver receiver) {
        this.receiver = receiver;
    }

    private void sendSessionData(Class c, Bundle b) {
        Intent i = new Intent(context, ServerService.class);
        i.putExtra(ServerService.ACTION, ServerService.START_SESSION);
        i.putExtra(ServerService.CLASS, c);
        i.putExtra(ServerService.BUNDLE, b);
        context.startService(i);
    }

    private void registerMessageBroadcastReceiver() {
        BroadcastReceiver messageReceiver = new EventBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UI_EVENT);
        LocalBroadcastManager.getInstance(context).registerReceiver(messageReceiver, intentFilter);

        BroadcastReceiver connectedClientReceiver = new ConnectedClientBroadcastReceiver();
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(CONNECTED_CLIENT);
        LocalBroadcastManager.getInstance(context).registerReceiver(connectedClientReceiver, intentFilter2);
    }

    private class EventBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            SessionMessage message = (SessionMessage) intent.getExtras().getSerializable(SessionMessage.class.getName());
            if (receiver != null) {
                receiver.onUiEvent(message.getPayload());
            }
        }
    }

    private class ConnectedClientBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            RegisterMessage message = (RegisterMessage) intent.getExtras().getSerializable(RegisterMessage.class.getName());
            if (receiver != null) {
                receiver.onClientConnected(message.getPayload());
            }
        }
    }

    /*
     * This interface is used for callbacks from Server service.
     *  * onUiEvent is called when LocalSession instance send some data to UI
     *  * onClientConnected is called when new client connects
     */
    public interface OnUiEventReceiver {
        public void onUiEvent(Payload<?> payload);
        public void onClientConnected(Payload<?> payload);
    }
}
