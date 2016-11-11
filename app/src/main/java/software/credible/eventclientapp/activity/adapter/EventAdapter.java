package software.credible.eventclientapp.activity.adapter;

import android.content.Context;
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

import software.credible.eventclientapp.R;
import software.credible.eventclientapp.remote.dto.EventDto;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {
    private LayoutInflater layoutInflater;
    private List<EventDto> events = new ArrayList<>();

    public EventAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.event_item, parent, false);
        EventHolder eventHolder = new EventHolder(view);
        return eventHolder;
    }

    @Override
    public void onBindViewHolder(EventHolder holder, int position) {
        EventDto event = events.get(position);
        holder.setDetails(event.getDetails());
        holder.setSummary(event.getSummary());
        holder.setDateOfEvent(event.getEventDate());

    }

    public void prependResults(List<EventDto> results) {
        this.events.addAll(0, results);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return events == null ? 0 : events.size();
    }

    public static class EventHolder extends RecyclerView.ViewHolder {
        private TextView summary;
        private TextView details;
        private TextView dayOfMonth;
        private TextView monthName;

        public EventHolder(View itemView) {
            super(itemView);
            summary = (TextView) itemView.findViewById(R.id.summary);
            details = (TextView) itemView.findViewById(R.id.details);
            dayOfMonth = (TextView) itemView.findViewById(R.id.dayOfMonth);
            monthName = (TextView) itemView.findViewById(R.id.monthName);
        }

        public void setSummary(String text) {
            summary.setText(text);
        }

        public void setDetails(String text) {
            details.setText(text);
        }
        public void setDateOfEvent(Date date) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM:dd", Locale.US);
            String[] dateParts = sdf.format(date).split(":");
            monthName.setText(dateParts[0]);
            dayOfMonth.setText(dateParts[1]);
        }
    }
}
