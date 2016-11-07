package com.avoidit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class AddRuleActivity extends AppCompatActivity {
    private EditText mRuleName;

    private Button mLocationEntryButton;
    private Button mCategoryEntryButton;
    private Button mPriceEntryButton;

    private ListView mEntriesList;
    private ArrayAdapter<AbstractRuleEntry> mEntryAdapter;

    private RadioButton mTextMessageRadioButton;
    private RadioButton mAlarmRadioButton;

    private EditText mContactName;
    private EditText mContactPhonenumber;

    private EditText mPasses;
    private Button mSaveRuleButton;

    private View mProgressView;

    private PostRuleTask mPostRuleTask = null;


    private void initiateComponents(){
        this.mRuleName = (EditText) findViewById(R.id.ruleName);

        this.mLocationEntryButton = (Button) findViewById(R.id.location_entry_button);
        this.mCategoryEntryButton = (Button) findViewById(R.id.category_entry_button);
        this.mPriceEntryButton = (Button) findViewById(R.id.price_entry_button);

        this.mEntriesList = (ListView) findViewById(R.id.entries_listview);

        this.mTextMessageRadioButton = (RadioButton) findViewById(R.id.radioButtonTextMessage);
        this.mAlarmRadioButton = (RadioButton) findViewById(R.id.radioButtonAlarm);

        this.mContactName = (EditText) findViewById(R.id.contactName);
        this.mContactPhonenumber = (EditText) findViewById(R.id.contactPhonenumber);

        this.mPasses = (EditText) findViewById(R.id.numberOfPasses);

        this.mSaveRuleButton = (Button) findViewById(R.id.save_rule_button);

        this.mProgressView = findViewById(R.id.add_rule_progress);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rule);
        mEntryAdapter = new ArrayAdapter<AbstractRuleEntry>(this, android.R.layout.simple_list_item_1,
                RuleContainer.getInstance().getRuleUnderConstruction().entries);

        this.initiateComponents();
        mEntriesList.setAdapter(mEntryAdapter);

        this.mLocationEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LocationEntryActivity.class);
                startActivity(intent);
            }
        });
        this.mCategoryEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CategoryEntryActivity.class);
                startActivity(intent);
            }
        });
        this.mPriceEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PriceEntryActivity.class);
                startActivity(intent);
            }
        });

        // Set a standard value for number of passes
        this.mPasses.setText("3");

        this.mSaveRuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptAddRule(v);
            }
        });
    }

    private void attemptAddRule(View v){
        if (mPostRuleTask != null) {
            return;
        }

        // Reset errors.
        this.mRuleName.setError(null);
        this.mPasses.setError(null);
        this.mContactName.setError(null);
        this.mContactPhonenumber.setError(null);


        // Get current rules
        Rule rule = RuleContainer.getInstance().getRuleUnderConstruction();

        String ruleName = mRuleName.getText().toString();
        String alarmType = this.getAlarmType();
        String numberOfPasses = mPasses.getText().toString();
        String contactName = mContactName.getText().toString();
        String contactPhonenumber = mContactPhonenumber.getText().toString();

        boolean cancel = false;
        List<View> focusView = new LinkedList<>();

        if (TextUtils.isEmpty(ruleName)){
            mRuleName.setError("Rule name is required");
            focusView.add(mRuleName);
            cancel = true;
        }

        if (TextUtils.isEmpty(numberOfPasses)){
            mPasses.setError("Number of passes is required");
            focusView.add(mPasses);
            cancel = true;
        }

        if (TextUtils.isEmpty(contactName)){
            mContactName.setError("Contact name is required");
            focusView.add(mContactName);
            cancel = true;
        }

        if (TextUtils.isEmpty(contactPhonenumber)){
            mContactPhonenumber.setError("Contact phonenumber is required");
            focusView.add(mContactPhonenumber);
            cancel = true;
        }

        if (rule.entries.isEmpty()) {
            Snackbar.make(v,"Please add at least 1 entry.",Snackbar.LENGTH_SHORT).show();
            cancel = true;
        }

        if (cancel){
            for (View fv : focusView) {
                fv.requestFocus();
            }
        } else {
            rule.ruleName = ruleName;
            rule.alarmType = alarmType;
            rule.passes = numberOfPasses;
            rule.contactName = contactName;
            rule.contactPhonenumber = contactPhonenumber;

            // Used for debugging
            System.out.println(rule.toJson());

            showProgress(true);
            mPostRuleTask = new PostRuleTask(rule, this);
            mPostRuleTask.execute((Void) null);
        }
    }

    private String getAlarmType(){
        String alarmType = "";
        if (mTextMessageRadioButton.isChecked()){
            alarmType = "text";
        } else if (mAlarmRadioButton.isChecked()){
        }
        return alarmType;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEntryAdapter.notifyDataSetChanged();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        mSaveRuleButton.setEnabled(!show);

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

    public class PostRuleTask extends AsyncTask<Void, Void, Boolean> {

        private Rule mRule;
        private final Context mContext;


        public PostRuleTask(Rule rule, Context context) {
            mRule = rule;
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HttpHelper helper = new HttpHelper();
            boolean success = false;
            final String ENDPOINT = "/createrulewithentries";

            SharedPreferences settings = getSharedPreferences(RegistrationActivity.PREFS_NAME, 0);
            String token = settings.getString("token","null");

            // if we don't have a token it's not worth executing any code.
            if (!Objects.equals(token, "null")){

                // Response from server
                String response = HttpHelper.postJson(ENDPOINT, mRule.toJson(),token);
                try {
                    // Parse json response from server
                    JSONObject jsonResponse = new JSONObject(response);

                    // Refine message
                    JSONArray jsonResponseJSONArray = jsonResponse.getJSONArray("message");
                    String message = jsonResponseJSONArray.join("\n").replace("\"", "");

                    // If message contains success we should return true, else false
                    if (message.contains("success")){
                        success = true;
                    } else {
                        Snackbar.make(findViewById(R.id.add_rule_progress),message, Snackbar.LENGTH_LONG).show();
                        success = false;
                    }

                    return success;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mPostRuleTask = null;

            if (success) {
                finish();
                RuleContainer.getInstance().finalizeRuleUnderConstruction();
                Log.d("AddRule", "Successfully added Rule");
            } else {
                showProgress(false);
                Log.d("AddRule", "Failed to add Rule");
            }
        }

        @Override
        protected void onCancelled() {
            mPostRuleTask = null;
            showProgress(false);
        }
    }

    private void updateEntriesList() {
        ArrayAdapter<AbstractRuleEntry> entriesAdapter = new ArrayAdapter<AbstractRuleEntry>(this,
                android.R.layout.simple_list_item_1,
                RuleContainer.getInstance().getRuleUnderConstruction().entries);
        mEntriesList.setAdapter(entriesAdapter);
    }

}


