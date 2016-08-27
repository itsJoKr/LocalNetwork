package dev.jokr.localnet.discovery.models;

/**
 * Created by JoKr on 8/27/2016.
 */
public class DiscoveryReply {

    private String ip;
    private int port;

    public DiscoveryReply(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
