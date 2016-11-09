package com.avoidit;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ngraves3 on 11/9/16.
 */
public class LocationService extends IntentService {

    private static List<Intent> currentIntents = new LinkedList<>();

    private GoogleApiClient mApiClient;

    public LocationService() {
        super("LocationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();
    }

    public static List<Intent> getUsages() {
        return currentIntents;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        currentIntents.add(intent);

        Log.d("LocationService", "Checking appropriate permissions");
        if ((ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {
            Log.d("LocationService", "Don't have right permissions");
        } else {
            Log.d("LocationService", "Have right permissions");
        }

        Log.d("LocationService", "Connecting to API client");
        ConnectionResult result = mApiClient.blockingConnect(10, TimeUnit.SECONDS);

        if (result.isSuccess()) {
            Log.d("LocationService", "Connected to location service");
        } else {
            Log.d("LocationService", "Failed to connect to location service.");
        }

        while (true) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mApiClient);

            if (location != null) {
                HashMap<String, String> body = new HashMap<>();
                body.put("latitude", location.getLatitude() + "");
                body.put("longitude", location.getLongitude() + "");

                String resp = HttpHelper.post("/postlocation", body);

                if (resp == null) {
                    Log.d("LocationService", "Failed to post location");
                } else {
                    Log.d("LocationService", "Last known location: " + resp);
                }
            } else {
                Log.d("LocationService", "Failed to get location");
            }



            try {
                Thread.sleep(30 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
