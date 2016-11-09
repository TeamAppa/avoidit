package com.avoidit;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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

        Log.d("LocationService", "Connecting to API client");
        mApiClient.connect();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Log.d("LocationService", "Have appropriate permissions");

        while (true) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mApiClient);

            HashMap<String, String> body = new HashMap<>();
            body.put("latitude", location.getLatitude() + "");
            body.put("longitude", location.getLongitude() + "");

            String resp = HttpHelper.post("/postlocation", body);

            if (resp == null) {
                Log.d("LocationService", "Failed to post location");
            } else {
                Log.d("LocationService", "Last known location: " + resp);
            }

            try {
                Thread.sleep(30 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
