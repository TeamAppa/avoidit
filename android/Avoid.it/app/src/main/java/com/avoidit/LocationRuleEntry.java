package com.avoidit;

import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ngraves3 on 10/26/16.
 */
public class LocationRuleEntry extends AbstractRuleEntry<String> {

    public LocationRuleEntry(int ruleId, String typeName, String displayName, String locationId) {
        super(ruleId, typeName, displayName);
        mAvoidanceCriteria = locationId;
    }

    public LocationRuleEntry(String displayName, String locationId) {
        super("LO", displayName);
        mAvoidanceCriteria = locationId;
    }

    @Override
    protected void jsonifyCriteria(JSONObject json, String avoidanceCriteria) throws JSONException {
        json.put("location_id", mAvoidanceCriteria);
    }
}
