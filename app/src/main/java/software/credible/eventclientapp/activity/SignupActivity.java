package software.credible.eventclientapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncCredentials;
import io.realm.SyncUser;
import software.credible.eventclientapp.R;
import software.credible.eventclientapp.managers.UserManager;
import software.credible.eventclientapp.activity.helper.RoboAppCompatActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import static software.credible.eventclientapp.GroupChatApplication.AUTH_URL;

@ContentView(R.layout.activity_signup)
public class SignupActivity extends RoboAppCompatActivity {

    private static final String TAG = "SignupActivity";

    @InjectView(R.id.input_name) EditText team;
    @InjectView(R.id.input_email) EditText emailText;
    @InjectView(R.id.input_password) EditText passwordText;
    @InjectView(R.id.btn_signup) Button signupButton;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
    }

    public void goToLogin(View view) {
        Log.d(TAG, "Going back to performLogin");
        finish();
    }

    public void performSignUp(View view) {
        Log.d(TAG, "Signup");
        showProgress();
        final String teamName = team.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        SyncUser.loginAsync(SyncCredentials.usernamePassword(email, password, true), AUTH_URL, new SyncUser.Callback() {
            @Override
            public void onSuccess(SyncUser user) {
                UserManager.setActiveUser(user);
                UserManager.addUserToTeam(user.getIdentity(), teamName);
                hideProgress();
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                intent.setAction(LoginActivity.AUTO_LOGIN);
                startActivity(intent);
                finish();
           }

            @Override
            public void onError(ObjectServerError error) {
                hideProgress();
                String errorMsg;
                switch (error.getErrorCode()) {
                    case EXISTING_ACCOUNT: errorMsg = "Account already exists"; break;
                    default:
                        errorMsg = error.toString();
                }
                Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void showProgress() {
        signupButton.setEnabled(false);
        progressDialog.show();
    }

    private void hideProgress() {
        signupButton.setEnabled(true);
        progressDialog.dismiss();
    }

}
