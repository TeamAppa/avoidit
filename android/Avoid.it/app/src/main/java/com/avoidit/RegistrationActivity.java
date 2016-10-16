package com.avoidit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * A registration screen.
 */
public class RegistrationActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "AvoidItPrefs";

    /**
     * Keep track of the registration task to ensure we can cancel it if requested.
     */
    private RegistrationTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPhoneNumberView;
    private EditText mNameView;
    private EditText mConfirmPasswordView;

    private View mProgressView;
    private View mRegistrationFormView;
    Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        // Set up the registration form.
        mEmailView = (EditText) findViewById(R.id.email);
        mNameView = (EditText) findViewById(R.id.firstName);
        mPhoneNumberView = (EditText) findViewById(R.id.phoneNumber);
        mPasswordView = (EditText) findViewById(R.id.password);
        mConfirmPasswordView = (EditText) findViewById(R.id.confirm_password);
        mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });
        mRegisterButton.setEnabled(true);

        mRegistrationFormView = findViewById(R.id.registration_form);
        mProgressView = findViewById(R.id.registration_progress);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
    }

    /**
     * Called when the user clicks the link to go to login.
     * Starts the login activity.
     * @param view The clicked view
     */
    public void startLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Attempts to register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual registration attempt is made.
     */
    private void attemptRegistration() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mNameView.setError(null);
        mPhoneNumberView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mConfirmPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confirmPassword = mConfirmPasswordView.getText().toString();
        String phoneNumber = mPhoneNumberView.getText().toString();
        String firstName = mNameView.getText().toString();

        boolean cancel = false;
        List<View> focusView = new LinkedList<>();

        // Check for a valid first name.
        if (TextUtils.isEmpty(firstName)) {
            mNameView.setError("Name is required");
            focusView.add(mNameView);
            cancel = true;
        }

        // Remove common non-digit characters from the phone number.
        String parsedPhone = phoneNumber.replaceAll("[- ()]+", "");

        if (TextUtils.isEmpty(phoneNumber) || !TextUtils.isDigitsOnly(parsedPhone)) {
            mPhoneNumberView.setError("Invalid phone number");
            focusView.add(mPhoneNumberView);
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView.add(mPasswordView);
            cancel = true;
        }

        // Check that the entered passwords match.
        if (TextUtils.isEmpty(confirmPassword) || !password.equals(confirmPassword)) {
            mConfirmPasswordView.setError(getString(R.string.error_mismatched_password));
            focusView.add(mConfirmPasswordView);
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView.add(mEmailView);
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView.add(mEmailView);
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt registation and focus each
            // form field with an error.
            for (View fv : focusView) {
                fv.requestFocus();
            }

        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new RegistrationTask(firstName, parsedPhone,
                    email, password, this);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        mRegisterButton.setEnabled(!show);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public class RegistrationTask extends AsyncTask<Void, Void, Boolean> {

        private final String mFirstName;
        private final String mPhoneNumber;
        private final String mEmail;
        private final String mPassword;
        private final Context mContext;

        RegistrationTask(String firstName, String phoneNumber,
                         String email, String password, Context context) {
            mFirstName = firstName;
            mPhoneNumber = phoneNumber;
            mEmail = email;
            mPassword = password;
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt registration - send params over network somehow.

            HashMap<String, String> param = new HashMap<>();
            param.put("first_name", mFirstName);
            param.put("phone", mPhoneNumber);
            param.put("email", mEmail);
            param.put("password", mPassword);

            String response = PostHelper.post("/createuser", param);

            try {
                JSONObject json_resp = new JSONObject(response);
                boolean success = json_resp.has("token");

                if (success) {
                    String token = json_resp.get("token").toString();

                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("token", token);

                    editor.commit();
                } else {
                    JSONArray errors = json_resp.getJSONArray("message");
                    String err_msg = errors.join("\n").replace("\"", "");

                    Log.d("com.avoidit", err_msg);

                    // Display error message somewhere.
                    Snackbar.make(findViewById(R.id.registration_progress),
                            R.string.app_name, Snackbar.LENGTH_LONG).setText(err_msg).show();
                }

                return success;

            } catch (JSONException je) {
                Log.d("com.avoid.it", "JSON Error");
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                finish();
                // TODO: Start HomeActivity?
                Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);

            } else {
                showProgress(false);
                // TODO: Implement error notification.
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

    }
}

