package dev.jokr.localnet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import dev.jokr.localnet.models.Payload;

/**
 * Created by JoKr on 8/28/2016.
 */
public class LocalServer {

    private Context context;

    public LocalServer(Context context) {
        this.context = context;
    }

    public void init() {
        Intent i = new Intent(context, ServerService.class);
        Log.d("USER", "starting service");
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
        i.putExtra(ServerService.ACTION, ServerService.START_SESSION);
        context.startService(i);
    }

    public void localSessionEvent(Payload<?> payload) {
        Intent i = new Intent(context, ServerService.class);
        i.putExtra(ServerService.ACTION, ServerService.SESSION_EVENT);
        i.putExtra(ServerService.PAYLOAD, payload);
        context.startService(i);
    }


}
