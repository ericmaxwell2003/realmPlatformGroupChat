package software.credible.eventclientapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import software.credible.eventclientapp.R;
import software.credible.eventclientapp.activity.helper.RoboAppCompatActivity;
import software.credible.eventclientapp.remote.AuthenticationService;
import software.credible.eventclientapp.remote.dto.LoginDto;
import software.credible.eventclientapp.remote.dto.OAuthTokenDto;
import software.credible.eventclientapp.remote.dto.RegistrationDto;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_signup)
public class SignupActivity extends RoboAppCompatActivity {

    private static final String TAG = "SignupActivity";

    @Inject private AuthenticationService authenticationService;

    @InjectView(R.id.input_name) EditText nameText;
    @InjectView(R.id.input_email) EditText emailText;
    @InjectView(R.id.input_username) EditText userName;
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

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String username = userName.getText().toString();
        String password = passwordText.getText().toString();

        SignUpTask signUpTask = new SignUpTask();
        signUpTask.execute(name, email, username, password);
    }


    private class SignUpTask extends AsyncTask<String, Void, RegistrationDto> {

        @Override
        protected RegistrationDto doInBackground(String... registrationDetails) {
            RegistrationDto registrationDto = new RegistrationDto();
            registrationDto.setFullName(registrationDetails[0]);
            registrationDto.setEmail(registrationDetails[1]);
            registrationDto.setUsername(registrationDetails[2]);
            registrationDto.setPassword(registrationDetails[3]);
            try {
                registrationDto = authenticationService.register(registrationDto);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            return registrationDto;
        }

        @Override
        protected void onPostExecute(RegistrationDto registrationDto) {
            hideProgress();
            if(registrationDto == null) {
                Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    private void showProgress() {
        signupButton.setEnabled(false);
        progressDialog.show();
    }

    private void hideProgress() {
        signupButton.setEnabled(true);
        progressDialog.dismiss();
    }

}
