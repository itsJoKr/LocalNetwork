package dev.jokr.localnet.discovery;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import dev.jokr.localnet.discovery.models.DiscoveryReply;
import dev.jokr.localnet.utils.NetworkUtil;
import dev.jokr.localnet.utils.SerializationUtil;

/**
 * Created by JoKr on 8/27/2016.
 */
public class DiscoveryThread implements Runnable {

    private DatagramSocket socket;
    private DiscoveryReply reply;

    public DiscoveryThread(DiscoveryReply reply) {
        this.reply = reply;
    }

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
                // Receive broadcast packet
                byte[] buffer = new byte[15000];
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(receivePacket);
                Log.d("USER", "Received packet from: " + receivePacket.getAddress().getHostAddress());

                // Send reply
                byte[] replyPacket = SerializationUtil.serialize(reply);
                socket.send(new DatagramPacket(replyPacket, replyPacket.length, receivePacket.getAddress(), receivePacket.getPort()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
