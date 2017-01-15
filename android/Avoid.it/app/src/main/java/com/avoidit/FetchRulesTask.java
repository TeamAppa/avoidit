package com.avoidit;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ngraves3 on 11/9/16.
 */
public class FetchRulesTask extends AsyncTask<Void, Void, Boolean> {

    private String mToken;
    private boolean mExecuted;

    public FetchRulesTask(String token) {
        mToken = token;
        mExecuted = false;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        final String ENDPOINT = "/getrules";

        if (mExecuted) {
            return false;
        }

        // Get rules from server.
        String response = HttpHelper.getJson(ENDPOINT, mToken);

        // If request fails, return early.
        if (response == null) {
            return false;
        }

        try {
            // Parse json response from server
            JSONObject jsonResponse = new JSONObject(response);

            // Loop over all rules, and add to RuleContainer.

            for (Iterator<String> keys = jsonResponse.keys(); keys.hasNext(); ) {
                String key = keys.next();
                JSONObject ruleJson = jsonResponse.getJSONObject(key);

                // Build rule.
                Rule rule = new Rule(
                        Integer.parseInt(key),
                        ruleJson.getString("rule_name"),
                        "text",
                        ruleJson.getString("passes"),
                        ruleJson.getString("contact_name"),
                        ruleJson.getString("contact_phone")
                );

                // Add entries
                if (!jsonResponse.has("entries")) {
                    RuleContainer.getInstance().addRule(rule);
                    continue;
                }

                JSONArray entryJson = jsonResponse.getJSONArray("entries");
                for (int i = 0; i < entryJson.length(); i++) {
                    JSONObject entry = entryJson.getJSONObject(i);

                    int ruleId = entry.getInt("rule_id");
                    String typeName = entry.getString("type");
                    String displayName = entry.getString("display_name");

                    if (entry.has("category_title")) {
                        // Parse category rule
                        CategoryRuleEntry catEntry = new CategoryRuleEntry(
                                ruleId,
                                typeName,
                                displayName,
                                entry.getString("category_title")
                        );
                        rule.entries.add(catEntry);

                    } else if (entry.has("price_list")) {
                        // Parse price rule
                        JSONArray jsonList = entry.getJSONArray("price_list");
                        List<String> priceList = new ArrayList<>();
                        for (int j = 0; j < jsonList.length(); j++) {
                            priceList.add(jsonList.getString(j));
                        }

                        rule.entries.add(new PriceRuleEntry(priceList));
                    } else if (entry.has("location_id")) {
                        // Parse location rule.
                        LocationRuleEntry locEntry = new LocationRuleEntry(
                                ruleId,
                                typeName,
                                displayName,
                                entry.getString("location_id")
                        );
                        rule.entries.add(locEntry);
                    } else {
                        // Handle error.
                        Log.d("GetRules - parse entry", "Error parsing entry: "
                                + entryJson.toString());
                    }

                }

                RuleContainer.getInstance().addRule(rule);
            }

            mExecuted = true;
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {

    }

    @Override
    protected void onCancelled() {
    }
}
