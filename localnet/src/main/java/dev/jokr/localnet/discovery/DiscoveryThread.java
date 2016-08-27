package dev.jokr.localnet.discovery;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import dev.jokr.localnet.utils.NetworkUtil;

/**
 * Created by JoKr on 8/27/2016.
 */
public class DiscoveryThread implements Runnable {

    DatagramSocket socket;
    // These are my favorite numbers. I need them hardcoded because you can't broadcast over all ports

    @Override
    public void run() {
        try {
            int port = NetworkUtil.BASE_PORT;
            do {
                socket = new DatagramSocket(port, InetAddress.getByName("0.0.0.0"));
                port--;
            } while (socket == null);

            socket.setBroadcast(true);

            while (true) {
                Log.d("USER", "Ready to receive packet");
                byte[] buffer = new byte[15000];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);socket.receive(packet);
                Log.d("USER", "Received packet from: " + packet.getAddress().getHostAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
