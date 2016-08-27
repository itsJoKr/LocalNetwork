package dev.jokr.localnet.discovery;

/**
 * Created by JoKr on 8/27/2016.
 */
public class DiscoverySession {

    public static DiscoverySession createDiscoverySession() {
        DiscoverySession instance = new DiscoverySession();
        instance.setRunnable(new DiscoveryThread());
        return instance;
    }

    public static DiscoverySession joinDiscoverSession() {
        DiscoverySession instance = new DiscoverySession();
        instance.setRunnable(new JoinThread());
        return instance;
    }

    private Runnable runnable;

    private DiscoverySession() { }

    private void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public void start() {
        new Thread(runnable).start();
    }
}
