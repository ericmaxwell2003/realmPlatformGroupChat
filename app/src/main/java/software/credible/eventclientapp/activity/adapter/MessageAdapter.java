package software.credible.eventclientapp.activity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import software.credible.eventclientapp.R;
import software.credible.eventclientapp.model.Message;

public class MessageAdapter extends RealmRecyclerViewAdapter<Message, MessageAdapter.MessageHolder> {

    public MessageAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Message> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        Message message = getItem(position);
        if(message != null) {
            holder.setMessageText(message.getContents());
        }
    }

    public static class MessageHolder extends RecyclerView.ViewHolder {

        private TextView messageText;

        public MessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.summary);
        }

        public void setMessageText(String text) {
            messageText.setText(text);
        }

    }
}
