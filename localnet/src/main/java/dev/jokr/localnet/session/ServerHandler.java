package dev.jokr.localnet.session;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by JoKr on 8/27/2016.
 */
class ServerHandler extends Handler {

    InitCallback callback;

    public ServerHandler(Looper looper, InitCallback callback) {
        super(looper);
        this.callback = callback;
    }

    @Override
    public void handleMessage(Message msg) {
        Log.d("USER", "Handler - message: " + msg.getData().getString("key"));

        try {
            final ServerSocket socket = new ServerSocket(0);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    callback.onInitializedSocket(socket.getLocalPort());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface InitCallback {
        public void onInitializedSocket(int port);
    }
}
