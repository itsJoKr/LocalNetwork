package dev.jokr.localnet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import dev.jokr.localnet.discovery.ClientJoinHandler;
import dev.jokr.localnet.discovery.models.DiscoveryReply;
import dev.jokr.localnet.models.Payload;
import dev.jokr.localnet.models.SessionMessage;

/**
 * Created by JoKr on 8/28/2016.
 */
public class LocalClient implements ClientJoinHandler.ServerDiscoveryCallback {
    /*
     *  Class for local client.
     */

    public static final String SESSION_MESSAGE = "session_message";

    private Context context;
    private MessageReceiver messageReceiver;
    private DiscoveryStatusReceiver discoveryReceiver;

    private Payload<?> registerPayload;


    public LocalClient(Context context) {
        this.context = context;
        registerMessageBroadcastReceiver();
    }

    public LocalClient(Context context, MessageReceiver receiver) {
        this.messageReceiver = receiver;
        this.context = context;
        registerMessageBroadcastReceiver();
    }


    /*
     * Attempt to discover server. If successful, onServerDiscovered will be called, otherwise
     * onDiscoveryTimeout will be called. You can call this method multiple times, if first attempt fails.
     */
    public void connect(Payload<?> payload) {
        registerPayload = payload;
        ClientJoinHandler thread = new ClientJoinHandler(this);
        new Thread(thread).start();
    }

    /*
     * When session starts you can send messages using this method.
     */
    public void sendSessionMessage(Payload<?> payload) {
        Intent i = new Intent(context, ClientService.class);
        i.putExtra(ClientService.ACTION, ClientService.SESSION_MESSAGE);
        i.putExtra(ClientService.PAYLOAD, payload);
        context.startService(i);
    }

    /*
     * Set interface implementation for receiving session messages from server
     */
    public void setReceiver(MessageReceiver receiver) {
        this.messageReceiver = receiver;
    }

    /*
     * Set interface implementation for receiving discovery phase events
     */
    public void setDiscoveryReceiver(DiscoveryStatusReceiver discoveryReceiver) {
        this.discoveryReceiver = discoveryReceiver;
    }

    /*
     * Stops the service
     */
    public void shutdown() {
        Intent i = new Intent(context, ClientService.class);
        context.stopService(i);
    }

    @Override
    public void serverDiscovered(DiscoveryReply reply) {
        if (discoveryReceiver != null)
            discoveryReceiver.onServerDiscovered();
        Intent i = new Intent(context, ClientService.class);
        i.putExtra(ClientService.ACTION, ClientService.DISCOVERY_REQUEST);
        i.putExtra(ClientService.PAYLOAD, registerPayload);
        i.putExtra(ClientService.DISCOVERY_REPLY, reply);
        context.startService(i);
    }

    @Override
    public void serverDiscoveryTimeout() {
        if (discoveryReceiver != null)
            discoveryReceiver.onDiscoveryTimeout();
    }

    private void registerMessageBroadcastReceiver() {
        BroadcastReceiver receiver = new MessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SESSION_MESSAGE);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);
    }

    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            SessionMessage message = (SessionMessage) intent.getExtras().getSerializable(SessionMessage.class.getName());
            if (message.getSignal() == SessionMessage.NONE) {
                if (messageReceiver != null) messageReceiver.onMessageReceived(message.getPayload());
            } else if (message.getSignal() == SessionMessage.START) {
                if (discoveryReceiver != null) discoveryReceiver.onSessionStart();
            }
        }
    }

    /*
     * Interface for receiving session messages from server
     */
    public interface MessageReceiver {
        public void onMessageReceived(Payload<?> payload);
    }

    /*
     * Interface for receiving events from discovery phase.
     */
    public interface DiscoveryStatusReceiver {
        public void onDiscoveryTimeout();
        public void onServerDiscovered();
        public void onSessionStart();
    }
}
