package software.credible.eventclientapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import software.credible.eventclientapp.R;
import software.credible.eventclientapp.activity.adapter.EventAdapter;
import software.credible.eventclientapp.activity.helper.RoboAppCompatActivity;
import software.credible.eventclientapp.remote.EventService;
import software.credible.eventclientapp.remote.TokenHolder;
import software.credible.eventclientapp.remote.dto.EventDto;
import software.credible.eventclientapp.remote.dto.EventSearchResultDto;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_browse_events)
public class BrowseEventsActivity extends RoboAppCompatActivity  {

    private static final String TAG = "BrowseEventsActivity";

    private EventAdapter adapter;
    private RecyclerView recycler;
    private boolean isFetching;

    private ProgressDialog progressDialog;
    @Inject private EventService eventService;
    @Inject private TokenHolder tokenHolder;
    private String lastSyncToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching New Events...");

        adapter = new EventAdapter(this);
        fetchNewEvents();

        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_browse_event, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                fetchNewEvents();
                return true;
            case R.id.action_logout:
                tokenHolder.setToken(null);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fetchNewEvents() {
        if(!isFetching) { // don't fetch if in the middle of fetching.
            FetchEventsTask fetchEventsTask = new FetchEventsTask();
            fetchEventsTask.execute();
        }
    }

    private class FetchEventsTask extends AsyncTask<Void, Void, List<EventDto>> {

        @Override
        protected void onPreExecute() {
            isFetching = true;
            showProgress();
        }

        @Override
        protected List<EventDto> doInBackground(Void... params) {
            try {
                EventSearchResultDto result = eventService.fetchEvents(lastSyncToken);
                lastSyncToken = result.getSyncToken();
                return result.getEvents();

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<EventDto> eventDtos) {
            hideProgress();
            isFetching = false;
            if(!eventDtos.isEmpty()) {
                adapter.prependResults(eventDtos);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(BrowseEventsActivity.this, "No Events Returned", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showProgress() {
        progressDialog.show();
    }

    private void hideProgress() {
        progressDialog.dismiss();
    }


}
