package dev.jokr.localnetworkapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dev.jokr.localnet.discovery.DiscoverySession;

public class MainActivity extends AppCompatActivity {

    Button btnJoin;
    Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreate = (Button) findViewById(R.id.btn_create);
        btnJoin = (Button) findViewById(R.id.btn_join);
    }


    public void createSession(View view) {
        btnCreate.setEnabled(false);
        btnJoin.setEnabled(false);

        DiscoverySession.createDiscoverySession().start();
    }

    public void joinSession(View view) {
        btnCreate.setEnabled(false);
        btnJoin.setEnabled(false);

        DiscoverySession.joinDiscoverSession().start();
    }
}
