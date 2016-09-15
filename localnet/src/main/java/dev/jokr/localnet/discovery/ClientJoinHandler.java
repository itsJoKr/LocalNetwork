package dev.jokr.localnet.discovery;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import dev.jokr.localnet.discovery.models.DiscoveryReply;
import dev.jokr.localnet.utils.NetworkUtil;
import dev.jokr.localnet.utils.SerializationUtil;

/**
 * Created by JoKr on 8/27/2016.
 */
public class ClientJoinHandler implements Runnable {

    private DatagramSocket socket;
    private ServerDiscoveryCallback callback;

    public ClientJoinHandler(ServerDiscoveryCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            socket = new DatagramSocket(0);
            socket.setBroadcast(true);

            // data is not important for braodcast discovery request
            byte[]  sendData = "DISCOVER_ME".getBytes();
            InetAddress broadcastAddr = InetAddress.getByName("255.255.255.255");

            int basePort = NetworkUtil.BASE_PORT;
            for (int i=0; i<5; i++) {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcastAddr, basePort-i);
                socket.send(sendPacket);
                Log.d("USER", "Packet sent to 255.255.255.255:" + (basePort-i));
            }
            byte[] buffer = new byte[15000];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            socket.setSoTimeout(2500);
            socket.receive(receivePacket);
            Log.d("USER", "Received packet from: " + receivePacket.getAddress().getHostAddress());

            byte[] bytes = receivePacket.getData();
            DiscoveryReply reply = (DiscoveryReply) SerializationUtil.deserialize(bytes);
            Log.d("USER", "Reply: " + reply.getIp() + ":" + reply.getPort());
            discoveryReply(reply);
        } catch (SocketTimeoutException e) {
            discoveryTimeout();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void discoveryReply(final DiscoveryReply reply) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                callback.serverDiscovered(reply);
            }
        });
    }

    private void discoveryTimeout() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                callback.serverDiscoveryTimeout();
            }
        });
    }

    public interface ServerDiscoveryCallback {
        public void serverDiscovered(DiscoveryReply reply);
        public void serverDiscoveryTimeout();
    }
}
