package dev.jokr.localnetworkapp.session;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import dev.jokr.localnet.LocalClient;
import dev.jokr.localnet.LocalServer;
import dev.jokr.localnet.models.Payload;
import dev.jokr.localnetworkapp.MyMessage;
import dev.jokr.localnetworkapp.R;

/**
 * Created by JoKr on 9/3/2016.
 */
public class SessionFragment extends Fragment {


    public static final String ROLE = "role";
    public static final int SERVER = 1;
    public static final int CLIENT = 2;

    private int role;
    private RecyclerView recyclerView;
    private MessagesAdapter adapter;
    private LocalClient client;
    private LocalServer server;

    public static SessionFragment newInstance(int role) {

        Bundle args = new Bundle();
        args.putInt(ROLE, role);
        SessionFragment fragment = new SessionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session, container, false);


        this.role = getArguments().getInt(ROLE);
        recyclerView = (RecyclerView) view.findViewById(R.id.list_messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MessagesAdapter(LayoutInflater.from(getContext()));
        recyclerView.setAdapter(adapter);

        Button btnSend = (Button) view.findViewById(R.id.btn_send_message);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });


        if (role == SERVER)
            setupServer();
        else if (role == CLIENT)
            setupClient();

        return view;
    }

    private void setupClient() {
        client = new LocalClient(getContext());
        client.setReceiver(new LocalClient.MessageReceiver() {
            @Override
            public void onMessageReceived(Payload<?> payload) {
                if (getContext() != null)
                    Toast.makeText(getContext(), "" + payload.getPayload(), Toast.LENGTH_LONG).show();
                else
                    Log.e("USER", "Received but context is null: " + payload.getPayload());
            }
        });
    }

    private void setupServer() {
        server = new LocalServer(getContext());
        server.setReceiver(new LocalServer.OnUiEventReceiver() {
            @Override
            public void onUiEvent(Payload<?> payload) {
                Toast.makeText(getContext(), "EVENT: " + payload.getPayload(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onClientConnected(Payload<?> payload) {
                // not interested (not in this version)
            }
        });
    }

    private void sendMessage() {
        if (role == SERVER)
            server.sendLocalSessionEvent(new Payload<MyMessage>(new MyMessage("This is something from server!")));
        else if (role == CLIENT)
            client.sendSessionMessage(new Payload<MyMessage>(new MyMessage("This is something from client!")));
    }


}
