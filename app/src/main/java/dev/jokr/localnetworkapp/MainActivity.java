package dev.jokr.localnetworkapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dev.jokr.localnet.LocalPeer;
import dev.jokr.localnet.LocalServer;

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
        //btnCreate.setEnabled(false);
        btnJoin.setEnabled(false);

//        Intent i = new Intent(this, ServerSocketService.class);
//        i.putExtra("key", "yolo");
//        Log.d("USER", "starting service");
//        startService(i);

        LocalServer localServer = new LocalServer(this);
        localServer.init();
    }

    public void joinSession(View view) {
        btnCreate.setEnabled(false);
        btnJoin.setEnabled(false);

        LocalPeer localPeer = new LocalPeer(this);
        localPeer.connect();
    }
}
