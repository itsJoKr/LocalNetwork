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

    public static final String UI_EVENT = "ui_event";
    public static final String CONNECTED_CLIENT = "conn_client";

    private Context context;
    private OnUiEventReceiver receiver;

    public LocalServer(Context context) {
        this.context = context;
        registerMessageBroadcastReceiver();
    }

    public void init() {
        Intent i = new Intent(context, ServerService.class);
        context.startService(i);
    }

    public void setSession(Class c) {
        sendSessionData(c, null);
    }

    public void setSession(Class c, Bundle b) {
        sendSessionData(c, b);
    }

    private void sendSessionData(Class c, Bundle b) {
        Intent i = new Intent(context, ServerService.class);
        i.putExtra(ServerService.ACTION, ServerService.START_SESSION);
        i.putExtra(ServerService.CLASS, c);
        i.putExtra(ServerService.BUNDLE, b);
        context.startService(i);
    }

    public void shutdown() {
        Intent i = new Intent(context, ServerService.class);
        i.putExtra(ServerService.ACTION, ServerService.STOP);
        context.startService(i);
    }

    public void sendLocalSessionEvent(Payload<?> payload) {
        Intent i = new Intent(context, ServerService.class);
        i.putExtra(ServerService.ACTION, ServerService.SESSION_EVENT);
        i.putExtra(ServerService.PAYLOAD, payload);
        context.startService(i);
    }

    public void setReceiver(OnUiEventReceiver receiver) {
        this.receiver = receiver;
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

    public interface OnUiEventReceiver {
        public void onUiEvent(Payload<?> payload);
        public void onClientConnected(Payload<?> payload);
    }
}
