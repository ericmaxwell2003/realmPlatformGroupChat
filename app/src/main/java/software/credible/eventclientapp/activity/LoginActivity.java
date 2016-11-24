package software.credible.eventclientapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.SyncCredentials;
import io.realm.ObjectServerError;
import io.realm.SyncUser;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import software.credible.eventclientapp.GroupChatApplication;
import software.credible.eventclientapp.R;
import software.credible.eventclientapp.managers.UserManager;
import software.credible.eventclientapp.activity.helper.RoboAppCompatActivity;

@ContentView(R.layout.activity_login)
public class LoginActivity extends RoboAppCompatActivity implements SyncUser.Callback {

    public static final String AUTO_LOGIN = "software.credible.eventclientapp.activity.LoginActivity.AUTO_LOGIN";
    private static final String TAG = "LoginActivity";

    @InjectView(R.id.input_username) EditText emailText;
    @InjectView(R.id.input_password) EditText passwordText;
    @InjectView(R.id.btn_login) Button loginButton;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");

        if(AUTO_LOGIN.equals(getIntent().getAction())) {
            final SyncUser user = SyncUser.currentUser();
            if (user != null) {
                loginComplete(user);
            }
        }
    }

    @Override
    public void onSuccess(SyncUser user) {
        hideProgress();
        loginComplete(user);
    }

    @Override
    public void onError(ObjectServerError error) {
        hideProgress();
        String errorMsg;
        switch (error.getErrorCode()) {
            case UNKNOWN_ACCOUNT:
                errorMsg = "Account does not exists.";
                break;
            case INVALID_CREDENTIALS:
                errorMsg = "The provided credentials are invalid!"; // This message covers also expired account token
                break;
            default:
                errorMsg = error.toString();
        }
        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_LONG).show();
    }

    public void goToSignUp(View view) {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
    }

    public void performLogin(View view) {
        Log.d(TAG, "Login");
        String username = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(getBaseContext(), "Username & Password required", Toast.LENGTH_LONG).show();
        } else {
            showProgress();
            SyncUser.loginAsync(
                    SyncCredentials.usernamePassword(username, password, false),
                    GroupChatApplication.AUTH_URL, this);
        }
    }

    public void loginComplete(SyncUser user) {
        UserManager.setActiveUser(user);
        Intent intent = new Intent(LoginActivity.this, MessageThreadActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProgress() {
        loginButton.setEnabled(false);
        progressDialog.show();
    }

    private void hideProgress() {
        loginButton.setEnabled(true);
        progressDialog.dismiss();
    }

}
