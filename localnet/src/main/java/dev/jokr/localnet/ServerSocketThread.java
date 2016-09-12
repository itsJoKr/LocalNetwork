package dev.jokr.localnet;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import dev.jokr.localnet.models.IncomingServerMessage;
import dev.jokr.localnet.models.RegisterMessage;
import dev.jokr.localnet.models.SessionMessage;
import dev.jokr.localnet.utils.MessageType;

/**
 * Created by JoKr on 8/28/2016.
 */
class ServerSocketThread implements Runnable {
    private ServiceCallback callback;

    public ServerSocketThread(ServiceCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            Log.d("USER", "starting ServerSocket...");
            final ServerSocket serverSocket = new ServerSocket(0);
            notifySocketInitialized(serverSocket.getLocalPort());

            while (true) {
                Socket socket = serverSocket.accept();
                Log.d("USER", "ServerSocket :: got incomingMessage");
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                IncomingServerMessage message = (IncomingServerMessage) input.readObject();
                if (message.getType().equals(MessageType.REGISTER)) {
                    notifyClientConnected((RegisterMessage) message.getMessage());
                } else if (message.getType().equals(MessageType.SESSION)) {
                    InetSocketAddress adr = (InetSocketAddress) socket.getRemoteSocketAddress();
                    notifySessionMessage((SessionMessage) message.getMessage(), adr.getAddress().getHostAddress());
                } else {
                    throw new IllegalArgumentException("Unknown message type: " + message.getType());
                }
            }
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

    private void notifyClientConnected(final RegisterMessage message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                callback.onClientConnected(message);
            }
        });
    }

    private void notifySessionMessage(final SessionMessage message, final String senderIp) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                callback.onSessionMessage(message, senderIp);
            }
        });
    }

    public interface ServiceCallback {
        public void onInitializedSocket(int port);
        public void onClientConnected(RegisterMessage message);
        public void onSessionMessage(SessionMessage message, String senderIp);
    }

}
