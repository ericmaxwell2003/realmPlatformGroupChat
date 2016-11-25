package software.credible.eventclientapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmResults;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import software.credible.eventclientapp.R;
import software.credible.eventclientapp.activity.adapter.MessageAdapter;
import software.credible.eventclientapp.activity.helper.RoboAppCompatActivity;
import software.credible.eventclientapp.managers.DataManager;
import software.credible.eventclientapp.model.Message;
import software.credible.eventclientapp.model.Team;

@ContentView(R.layout.activity_browse_events)
public class MessageThreadActivity extends RoboAppCompatActivity  {

    private static final String TAG = "MessageThreadActivity";

    private Realm realm;
    private MessageAdapter adapter;

    @InjectView(R.id.recycler)
    private RecyclerView recycler;

    @InjectView(R.id.messageText)
    private EditText messageTextBox;

    private ProgressDialog progressDialog;
    private RealmResults<Message> messages;
    private Team team;
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

        if(getSupportActionBar() != null) {
            team = DataManager.findCurrentTeam(realm);
            getSupportActionBar().setTitle(team.getTeamName());
        }

        messages = DataManager.findAllMessagesAsync(realm, team.getTeamName());
        messages.addChangeListener(new RealmChangeListener<RealmResults<Message>>() {
            @Override
            public void onChange(RealmResults<Message> element) {
                if(!initialLoadComplete && messages.isLoaded()) {
                    hideProgress();
                    initialLoadComplete = true;
                }
            }
        });

        adapter = new MessageAdapter(this, messages, true);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
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
                DataManager.removeAllMessagesAsync(realm);
                return true;
            case R.id.action_logout:
                closeRealm();
                DataManager.logoutActiveUser();
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
        closeRealm();
    }

    private void closeRealm() {
        if(realm != null) {
            realm.removeAllChangeListeners();
            realm.close();
            realm = null;
        }
    }

    public void sendMessage(View view) {
        if(!TextUtils.isEmpty(messageTextBox.getText())) {
            DataManager.addMessageAsync(messageTextBox.getText().toString());
            messageTextBox.setText("");
        }
    }

    private void showProgress() {
        progressDialog.show();
    }

    private void hideProgress() {
        progressDialog.dismiss();
    }


}
