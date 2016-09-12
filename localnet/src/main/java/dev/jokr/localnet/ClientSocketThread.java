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
class ClientSocketThread implements Runnable {

    private  ServiceCallback callback;

    public ClientSocketThread(ServiceCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
       ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("USER", "Listening on " + serverSocket.getLocalPort());
        notifySocketInitialized(serverSocket.getLocalPort());

        while(true) {
            try {
                Socket socket = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                SessionMessage sessionMessage = (SessionMessage) objectInputStream.readObject();
                passSessionMessage(sessionMessage);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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

    private void passSessionMessage(final SessionMessage message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                callback.onSessionMessage(message);
            }
        });
    }

    public interface ServiceCallback {
        public void onInitializedSocket(int port);
        public void onSessionMessage(SessionMessage message);
    }
}
