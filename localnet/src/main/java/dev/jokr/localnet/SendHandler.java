package dev.jokr.localnet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import dev.jokr.localnet.models.IncomingServerMessage;
import dev.jokr.localnet.models.SessionMessage;

/**
 * Created by JoKr on 8/29/2016.
 */
public class SendHandler implements Runnable {

    public static final String MESSAGE = "msg";
    public static final String ADDRESS = "addr";
    public static final String PORT = "port";

    private IncomingServerMessage message;
    private String address;
    private int port;

    public SendHandler(IncomingServerMessage message, String address, int port) {
        this.message = message;
        this.address = address;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(InetAddress.getByName(address), port);
            ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
            stream.writeObject(message);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
