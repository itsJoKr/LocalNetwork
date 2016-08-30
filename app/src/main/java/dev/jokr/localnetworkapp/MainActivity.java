package dev.jokr.localnetworkapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dev.jokr.localnet.LocalClient;
import dev.jokr.localnet.LocalServer;
import dev.jokr.localnet._LocalServer;

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

//        Intent i = new Intent(this, _ServerSocketService.class);
//        i.putExtra("key", "yolo");
//        Log.d("USER", "starting service");
//        startService(i);

//        _LocalServer localServer = new _LocalServer(this);
        LocalServer localServer = new LocalServer(this);
        localServer.init();
    }

    public void joinSession(View view) {
        btnCreate.setEnabled(false);

        LocalClient localClient = new LocalClient(this);
        localClient.connect();
    }
}
