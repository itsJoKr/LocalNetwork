package dev.jokr.localnetworkapp.discovery;

import android.content.Context;
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
import dev.jokr.localnetworkapp.GameSession;
import dev.jokr.localnetworkapp.R;
import dev.jokr.localnetworkapp.session.MessagesAdapter;

/**
 * Created by JoKr on 9/3/2016.
 */
public class DiscoveryFragment extends Fragment implements LocalServer.OnUiEventReceiver {

    Button btnJoin;
    Button btnCreate;
    Button btnStartSession;
    RecyclerView connectedClients;

    private LocalServer localServer;
    private boolean isServer;
    private MessagesAdapter adapter;
    private FragmentInteractionListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery, container, false);

        btnCreate = (Button) view.findViewById(R.id.btn_create);
        btnJoin = (Button) view.findViewById(R.id.btn_join);
        btnStartSession = (Button) view.findViewById(R.id.btn_start_session);
        connectedClients = (RecyclerView) view.findViewById(R.id.list_clients);
        connectedClients.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MessagesAdapter(LayoutInflater.from(getContext()));
        connectedClients.setAdapter(adapter);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSession();
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinSession();
            }
        });

        btnStartSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSession();
            }
        });

        return view;
    }


    public void createSession() {
        btnCreate.setEnabled(false);
        btnJoin.setEnabled(false);
        btnStartSession.setVisibility(View.VISIBLE);
        isServer = true;

        localServer = new LocalServer(getContext());
        localServer.setReceiver(this);
        localServer.init();
    }

    public void joinSession() {
        btnCreate.setEnabled(false);
        isServer = false;

        LocalClient localClient = new LocalClient(getContext());
        localClient.connect();
        localClient.setDiscoveryReceiver(new LocalClient.DiscoveryStatusReceiver() {
            @Override
            public void onDiscoveryTimeout() {
                Toast.makeText(getContext(), "Discovery timeout", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onServerDiscovered() {
                Toast.makeText(getContext(), "Server discovered!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSessionStart() {
                Log.d("USER", "Start client session!");
                if (listener != null) listener.onStartClientSession();
            }
        });
        localClient.setReceiver(new LocalClient.MessageReceiver() {
            @Override
            public void onMessageReceived(Payload<?> payload) {
                String toShow = "" + payload.getPayload();
                if (getContext() != null)
                    Toast.makeText(getContext(), toShow, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void startSession() {
        btnCreate.setEnabled(false);
        btnJoin.setEnabled(false);

        localServer.setSession(GameSession.class);
        if (listener != null) {
            listener.onStartSession();
        }
    }


    @Override
    public void onUiEvent(Payload<?> payload) {
        // UiEvent shouldn't happen during discovery because Session is not yet created
        //Toast.makeText(getContext(), "" + payload, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClientConnected(Payload<?> payload) {
        adapter.addMessage("" + payload);
    }

    public void setListener(FragmentInteractionListener listener) {
        this.listener = listener;
    }

    public interface FragmentInteractionListener {
        public void onStartSession();
        public void onStartClientSession();
    }
}
