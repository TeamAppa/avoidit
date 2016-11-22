package com.avoidit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LocationEntryActivity extends AppCompatActivity {

    private EditText mSearchWord;
    private Button mSearchButton;
    private ListView mResultList;

    private ArrayAdapter<LocationRuleEntry> mLocationAdapter;
    private List<LocationRuleEntry> mLocations;

    private View mProgressView;

    private GetLocationsTask mGetLocationsTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_entry);

        this.mProgressView = findViewById(R.id.get_categories_progress);

        this.mSearchWord = (EditText) findViewById(R.id.search_text);
        this.mSearchButton = (Button) findViewById(R.id.search_categories_button);
        this.mResultList = (ListView) findViewById(R.id.categories_listview);

        this.mLocations = new ArrayList<>();
        this.mLocationAdapter = new ArrayAdapter<LocationRuleEntry>(this, android.R.layout.simple_list_item_1,this.mLocations);
        this.mResultList.setAdapter(mLocationAdapter);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                attemptSearchLocation();
            }
        });

        this.mResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RuleContainer.getInstance().getRuleUnderConstruction().entries.add(mLocationAdapter.getItem(position));
                finish();
            }
        });

    }

    private void attemptSearchLocation() {
        if (mGetLocationsTask != null){
            return;
        }
        String term = this.mSearchWord.getText().toString();

        mGetLocationsTask = new GetLocationsTask(this,term);
        try {
            mGetLocationsTask.execute((Void) null).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        mSearchButton.setEnabled(!show);
        mResultList.setEnabled(!show);

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

    public class GetLocationsTask extends AsyncTask<Void, Void, Boolean> {
        private final Context mContext;
        private final String mSearchTerm;

        public GetLocationsTask(Context mContext, String mSearchTerm) {
            this.mContext = mContext;
            this.mSearchTerm = mSearchTerm;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HttpHelper helper = new HttpHelper();
            boolean success = false;

            final String ENDPOINT = "/search";

            SharedPreferences settings = getSharedPreferences(RegistrationActivity.PREFS_NAME, 0);
            String token = settings.getString("token", "null");


            // if we don't have a token it's not worth executing any code.
            if (!Objects.equals(token, "null")) {

                JSONObject jsonSearch = new JSONObject();
                try {
                    jsonSearch.put("latitude",LocationService.getmLatitude() + "");
                    jsonSearch.put("longitude",LocationService.getmLongitude() + "");
                    jsonSearch.put("term", mSearchTerm);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Response from server
                String response = HttpHelper.postJson(ENDPOINT, jsonSearch, token);
                try {
                    // Parse json response from server
                    JSONObject jsonResponse = new JSONObject(response);

                    // TODO: Improve check if response is valid
                    // Check if jsonResponse contains alias
                    if (jsonResponse.toString().contains("results")) {
                        success = true;

                        mLocations.clear();

                        JSONArray jsonArray = jsonResponse.getJSONArray("results");
                        for (int i=0; i < jsonArray.length(); i++){
                            JSONObject jO = jsonArray.getJSONObject(i);
                            JSONObject locationJO = jO.getJSONObject("location");
                            String location = locationJO.getString("address1") + " " + locationJO.getString("address2") + " " + locationJO.getString("address3") + " " + locationJO.getString("zip_code") + " " + locationJO.getString("country");

                            mLocations.add(new LocationRuleEntry(jO.getString("name") + " | " +
                                    location
                                    , jO.getString("id")));
                        }
                    } else {
                        Snackbar.make(findViewById(R.id.get_categories_progress), "", Snackbar.LENGTH_LONG).show();
                        success = false;
                    }

                    return success;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mGetLocationsTask = null;

            if (success) {
                Log.d("GetLocations", "Successfully got locations");
                mLocationAdapter.notifyDataSetChanged();
                showProgress(false);
            } else {
                showProgress(true
                );
                Log.d("GetLocations", "Failed to get locations");
            }
        }

        @Override
        protected void onCancelled() {
            mGetLocationsTask = null;
            showProgress(false);
        }


    }
}
