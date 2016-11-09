package com.avoidit;

import android.app.IntentService;
import android.content.Intent;

import java.util.HashMap;

/**
 * Created by ngraves3 on 11/9/16.
 */
public class LocationService extends IntentService {

    public LocationService() {
        super("LocationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while (true) {

        }
    }
}
