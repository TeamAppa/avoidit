package com.avoidit;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ngraves3 on 10/16/16.
 */
public class PostHelper {

    private static String server = "https://sheltered-scrubland-29626.herokuapp.com/avoiditapi";

    /**
     * Takes the endpoint and the set of params and posts it to the server.
     * @param endpoint The API endpoint.
     * @param params The key-value params for the JSON body.
     * @return The response returned from the server. Returns null on error.
     */
    public static String post(String endpoint, HashMap<String, String> params) {

        if (!endpoint.startsWith("/")) {
            endpoint = "/" + endpoint;
        }

        URL url;
        String response = "";
        boolean success = false;

        try {
            url = new URL(server + endpoint);

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Type", "application/json");

            // JSON Body
            JSONObject root = new JSONObject();

            for (String key : params.keySet()) {
                root.put(key, params.get(key));
            }

            String str = root.toString();
            OutputStream os = conn.getOutputStream();
            os.write(str.getBytes("UTF-8"));

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }

                success = true;
            } else {
                Log.d("com.avoidit", responseCode + "");
                success = false;
            }

            return response;

        } catch (Exception e) {
            Log.d("com.avoidit", e.getMessage());
            return null;
        }
    }
}
