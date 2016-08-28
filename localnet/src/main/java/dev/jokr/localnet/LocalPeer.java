package dev.jokr.localnet;

import android.content.Context;

import dev.jokr.localnet.discovery.JoinThread;

/**
 * Created by JoKr on 8/28/2016.
 */
public class LocalPeer {

    private Context context;

    public LocalPeer(Context context) {
        this.context = context;
    }

    public void connect() {
        JoinThread thread = new JoinThread();
        new Thread(thread).start();
    }
}
