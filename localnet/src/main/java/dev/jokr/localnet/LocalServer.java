package dev.jokr.localnet;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import dev.jokr.localnet.session._ServerSocketService;

/**
 * Created by JoKr on 8/28/2016.
 */
public class LocalServer {

    private Context context;

    public LocalServer(Context context) {
        this.context = context;
    }

    public void init() {
        Intent i = new Intent(context, ServerService.class);
        i.putExtra("key", "yolo");
        Log.d("USER", "starting service");
        context.startService(i);
    }
}
