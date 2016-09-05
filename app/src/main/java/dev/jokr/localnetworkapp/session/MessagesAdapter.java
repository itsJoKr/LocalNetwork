package dev.jokr.localnetworkapp.session;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dev.jokr.localnetworkapp.R;

/**
 * Created by JoKr on 9/3/2016.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageHolder> {

    private LayoutInflater inflater;
    private List<String> messages;

    public MessagesAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        this.messages = new ArrayList<>();
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageHolder(inflater.inflate(R.layout.item_message, parent, false));
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        holder.txtMessage.setText(messages.get(position));
    }

    public void addMessage(String message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageHolder extends RecyclerView.ViewHolder{
        TextView txtMessage;

        public MessageHolder(View itemView) {
            super(itemView);
            txtMessage = (TextView) itemView.findViewById(R.id.txt_message);
        }
    }

}
