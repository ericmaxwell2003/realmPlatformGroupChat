package software.credible.eventclientapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import software.credible.eventclientapp.R;
import software.credible.eventclientapp.activity.helper.RoboAppCompatActivity;
import software.credible.eventclientapp.remote.AuthenticationService;
import software.credible.eventclientapp.remote.TokenHolder;
import software.credible.eventclientapp.remote.dto.LoginDto;
import software.credible.eventclientapp.remote.dto.OAuthTokenDto;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_login)
public class LoginActivity extends RoboAppCompatActivity {

    private static final String TAG = "LoginActivity";

    @Inject private TokenHolder tokenHolder;
    @Inject private AuthenticationService authenticationService;

    @InjectView(R.id.input_username) EditText usernameText;
    @InjectView(R.id.input_password) EditText passwordText;
    @InjectView(R.id.btn_login) Button loginButton;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
    }

    public void goToSignUp(View view) {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
    }

    public void performLogin(View view) {
        Log.d(TAG, "Login");
        showProgress();

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        LoginTask loginTask = new LoginTask();
        loginTask.execute(username, password);
    }

    private void showProgress() {
        loginButton.setEnabled(false);
        progressDialog.show();
    }

    private void hideProgress() {
        loginButton.setEnabled(true);
        progressDialog.dismiss();
    }

    private class LoginTask extends AsyncTask<String, Void, OAuthTokenDto> {

        @Override
        protected OAuthTokenDto doInBackground(String... credentials) {
            LoginDto loginDto = new LoginDto();
            loginDto.setUsername(credentials[0]);
            loginDto.setPassword(credentials[1]);
            OAuthTokenDto oAuthTokenDto = null;
            try {
                oAuthTokenDto = authenticationService.login(loginDto);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            return oAuthTokenDto;
        }

        @Override
        protected void onPostExecute(OAuthTokenDto oAuthTokenDto) {
            hideProgress();
            if(oAuthTokenDto == null) {
                Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
            } else {
                tokenHolder.setToken(oAuthTokenDto);
                Intent intent = new Intent(LoginActivity.this, BrowseEventsActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

}
