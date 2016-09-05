package dev.jokr.localnetworkapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import dev.jokr.localnet.LocalClient;
import dev.jokr.localnet.LocalServer;
import dev.jokr.localnet.models.Payload;
import dev.jokr.localnetworkapp.discovery.DiscoveryFragment;
import dev.jokr.localnetworkapp.session.SessionFragment;

public class MainActivity extends AppCompatActivity implements DiscoveryFragment.FragmentInteractionListener {

    Button btnJoin;
    Button btnCreate;
    FrameLayout layoutMain;

    private LocalServer localServer;
    private boolean isServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreate = (Button) findViewById(R.id.btn_create);
        btnJoin = (Button) findViewById(R.id.btn_join);
        layoutMain = (FrameLayout) findViewById(R.id.layout_main);

        showDiscoveryFragment();
    }


    private void showDiscoveryFragment() {
        DiscoveryFragment fragment = new DiscoveryFragment();
        fragment.setListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_main, fragment)
                .commit();
    }

    @Override
    public void onStartSession() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_main, SessionFragment.newInstance(SessionFragment.SERVER))
                .commit();
    }

    @Override
    public void onStartClientSession() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_main, SessionFragment.newInstance(SessionFragment.CLIENT))
                .commit();
    }


//    public void createSession(View view) {
//        btnCreate.setEnabled(false);
//        btnJoin.setEnabled(false);
//        isServer = true;
//
//        localServer = new LocalServer(this);
//        localServer.init();
//    }
//
//    public void joinSession(View view) {
//        btnCreate.setEnabled(false);
//        isServer = false;
//
//        LocalClient localClient = new LocalClient(this);
//        localClient.connect();
//        localClient.setReceiver(new LocalClient.MessageReceiver() {
//            @Override
//            public void onMessageReceived(Payload<?> payload) {
//                Toast.makeText(MainActivity.this, "" + ((Integer)payload.getPayload()), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void startSession(View view) {
//        btnCreate.setEnabled(false);
//        btnJoin.setEnabled(false);
//
//        localServer.setSession(GameSession.class);
//    }
//
//
//    public void sendSampleMessage(View view) {
//        localServer.sendLocalSessionEvent(new Payload<Integer>(255));
//    }
}
