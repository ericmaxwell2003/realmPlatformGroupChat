package software.credible.eventclientapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import roboguice.inject.ContentView;
import software.credible.eventclientapp.R;
import software.credible.eventclientapp.activity.adapter.MessageAdapter;
import software.credible.eventclientapp.activity.helper.RoboAppCompatActivity;
import software.credible.eventclientapp.managers.MessageManager;
import software.credible.eventclientapp.managers.UserManager;
import software.credible.eventclientapp.model.Message;

@ContentView(R.layout.activity_browse_events)
public class MessageThreadActivity extends RoboAppCompatActivity  {

    private static final String TAG = "MessageThreadActivity";

    private Realm realm;
    private MessageAdapter adapter;
    private RecyclerView recycler;
    private ProgressDialog progressDialog;
    private RealmChangeListener<RealmResults<Message>> messageRealmChangeListener;
    private RealmResults<Message> messages;
    private boolean initialLoadComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance();

        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching New Messages...");

        initialLoadComplete = false;
        showProgress();

        messages = MessageManager.findAllMessagesAsync(realm);

        messageRealmChangeListener = new RealmChangeListener<RealmResults<Message>>() {
            @Override
            public void onChange(RealmResults<Message> element) {
                if(initialLoadComplete) {
                    return;
                } else if(messages.isLoaded()) {
                    hideProgress();
                    initialLoadComplete = true;
                }
            }
        };
        messages.addChangeListener(messageRealmChangeListener);

        adapter = new MessageAdapter(this, messages, true);

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
                makeFakeMessage();
                return true;
            case R.id.action_logout:
                realm.close();
                UserManager.logoutActiveUser();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void makeFakeMessage() {
        MessageManager.addMessageAsync(new Date().toString());
    }

    private void showProgress() {
        progressDialog.show();
    }

    private void hideProgress() {
        progressDialog.dismiss();
    }


}
