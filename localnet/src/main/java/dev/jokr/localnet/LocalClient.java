package dev.jokr.localnet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import dev.jokr.localnet.discovery.JoinThread;
import dev.jokr.localnet.discovery.models.DiscoveryReply;
import dev.jokr.localnet.models.Payload;
import dev.jokr.localnet.models.SessionMessage;

/**
 * Created by JoKr on 8/28/2016.
 */
public class LocalClient implements JoinThread.ServerDiscoveryCallback {

    public static final String SESSION_MESSAGE = "session_message";

    private Context context;
    private MessageReceiver receiver;

    public LocalClient(Context context) {
        this.context = context;
        registerMessageBroadcastReceiver();
    }

    public LocalClient(Context context, MessageReceiver receiver) {
        this.receiver = receiver;
        this.context = context;
        registerMessageBroadcastReceiver();
    }

    public void setReceiver(MessageReceiver receiver) {
        this.receiver = receiver;
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
            if (receiver != null)
                receiver.onMessageReceived(message.getPayload());
        }
    }

    public interface MessageReceiver {
        public void onMessageReceived(Payload<?> payload);
    }
}
