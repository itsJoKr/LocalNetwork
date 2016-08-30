package dev.jokr.localnet;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import dev.jokr.localnet.models.SessionMessage;

/**
 * Created by JoKr on 8/29/2016.
 */
public class ClientSocketHandler implements Runnable {

    private  ServiceCallback callback;

    public ClientSocketHandler(ServiceCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            final ServerSocket serverSocket = new ServerSocket(0);
            Log.d("USER", "Listening on " + serverSocket.getLocalPort());
            notifySocketInitialized(serverSocket.getLocalPort());
            Socket socket = serverSocket.accept();
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            SessionMessage<?> sessionMessage = (SessionMessage<?>) objectInputStream.readObject();
            Log.d("USER", "Session message!");



        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void notifySocketInitialized(final int port) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                callback.onInitializedSocket(port);
            }
        });
    }

    public interface ServiceCallback {
        public void onInitializedSocket(int port);
    }
}
