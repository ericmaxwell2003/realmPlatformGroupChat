package software.credible.eventclientapp.activity.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.SyncUser;
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
            if(message.getUserReference().getUserIdentity().equals(SyncUser.currentUser().getIdentity())) {
                holder.messageContainer.setGravity(Gravity.END);
                holder.messageText.setBackground(context.getDrawable(R.drawable.message_background_me));
            } else {
               holder.messageContainer.setGravity(Gravity.START);
                holder.messageText.setBackground(context.getDrawable(R.drawable.message_background_other));
            }
            holder.setMessageText(message.getContents());
        }
    }

    public static class MessageHolder extends RecyclerView.ViewHolder {

        private TextView messageText;
        private RelativeLayout messageContainer;

        public MessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.messageText);
            messageContainer = (RelativeLayout) itemView.findViewById(R.id.messageContainter);
        }

        public void setMessageText(String text) {
            messageText.setText(text);
        }

    }
}
