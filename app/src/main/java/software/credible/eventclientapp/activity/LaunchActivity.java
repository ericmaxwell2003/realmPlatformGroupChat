package software.credible.eventclientapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import javax.inject.Inject;

import software.credible.eventclientapp.R;
import software.credible.eventclientapp.activity.helper.RoboAppCompatActivity;
import software.credible.eventclientapp.remote.TokenHolder;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_launch)
public class LaunchActivity extends RoboAppCompatActivity {

    @Inject
    private TokenHolder tokenHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = null;
        // If we don't have a token, yet proceed to the login page.
        if(isOauthTokenPresent()) {
            intent = new Intent(this, BrowseEventsActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }

    private boolean isOauthTokenPresent() {
        return tokenHolder.getToken() != null && TextUtils.isEmpty(tokenHolder.getToken().getAccessToken());
    }

}
