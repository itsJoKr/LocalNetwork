package dev.jokr.localnet.discovery.models;

/**
 * Created by JoKr on 8/27/2016.
 */
public class Peer {

    private int id;
    private String ip;
    private int port;
    private String name;

    public Peer(int id, String ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
    }



    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
