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
public class JoinThread implements Runnable {

    DatagramSocket socket;

    @Override
    public void run() {
        try {
            socket = new DatagramSocket(0);
            socket.setBroadcast(true);

            byte[]  sendData = "DISCOVER_ME".getBytes();
            InetAddress broadcastAddr = InetAddress.getByName("255.255.255.255");

            int basePort = NetworkUtil.BASE_PORT;
            for (int i=0; i<5; i++) {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcastAddr, basePort-i);
                socket.send(sendPacket);
                Log.d("USER", "Packet sent to 255.255.255.255:" + (basePort-i));
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
