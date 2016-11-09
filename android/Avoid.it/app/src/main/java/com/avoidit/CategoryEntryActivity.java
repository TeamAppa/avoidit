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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryEntryActivity extends AppCompatActivity {

    private ListView mCategoriesList;
    private ArrayAdapter<CategoryEntry> mCategoryAdapter;
    private List<CategoryEntry> mCategories;

    private View mProgressView;

    private GetCategoriesTask mGetCategoriesTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_entry);

        this.mProgressView = findViewById(R.id.get_categories_progress);

        this.mCategoriesList = (ListView) findViewById(R.id.categories_listview);
        this.mCategories = new ArrayList<>();
        this.mCategoryAdapter = new ArrayAdapter<CategoryEntry>(this, android.R.layout.simple_list_item_1, mCategories);
        this.mCategoriesList.setAdapter(mCategoryAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGetCategoriesTask = new GetCategoriesTask(this);
        mGetCategoriesTask.execute((Void) null);

        mCategoryAdapter.notifyDataSetChanged();

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

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

    public class GetCategoriesTask extends AsyncTask<Void, Void, Boolean>{
        private final Context mContext;

        public GetCategoriesTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HttpHelper helper = new HttpHelper();
            boolean success = false;

            final String ENDPOINT = "/getcategoriesjson";

            SharedPreferences settings = getSharedPreferences(RegistrationActivity.PREFS_NAME, 0);
            String token = settings.getString("token","null");

            // if we don't have a token it's not worth executing any code.
            if (!Objects.equals(token, "null")){

                // Response from server
                String response = HttpHelper.getJson(ENDPOINT, token);
                try {
                    // Parse json response from server
                    JSONArray jsonResponse = new JSONArray(response);

                    // TODO: Improve check if response is valid
                    // Check if jsonResponse contains alias
                    if (jsonResponse.toString().contains("alias")){
                        success = true;

                        mCategories = new ArrayList<>();
                        for (int i=0; i<jsonResponse.length();i++){
                            JSONObject jO = jsonResponse.getJSONObject(i);
                            mCategories.add(new CategoryEntry(jO.get("title").toString(),jO.getJSONArray("alias")));
                        }
                        System.out.println(mCategories);
                    } else {
                        Snackbar.make(findViewById(R.id.get_categories_progress),"", Snackbar.LENGTH_LONG).show();
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
            if (success) {
                Log.d("GetCategories", "Successfully got categories");
            } else {
                showProgress(false);
                Log.d("GetCategories", "Failed to get categories");
            }
        }

        @Override
        protected void onCancelled() {
            mGetCategoriesTask = null;
            showProgress(false);
        }


    }
}


