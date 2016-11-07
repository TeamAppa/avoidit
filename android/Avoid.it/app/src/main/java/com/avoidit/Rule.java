package com.avoidit;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.io.FileDescriptor.out;

/**
 * Created by Anthon on 2016-11-06.
 */

public class Rule {
    public String ruleName;
    public String alarmType;
    public String passes = "3";
    public String contactName;
    public String contactPhonenumber;
    public List<AbstractRuleEntry> entries;

    public Rule() {
        this.ruleName = "";
        this.alarmType = "text";
        this.passes = "3";
        this.contactName = "";
        this.contactPhonenumber = "";
        this.entries = new ArrayList<>();
    }

    public Rule(String ruleName, String alarmType, String passes, String contactName, String contactPhonenumber) {
        this.ruleName = ruleName;
        this.alarmType = alarmType;
        this.passes = passes;
        this.contactName = contactName;
        this.contactPhonenumber = contactPhonenumber;
        this.entries = new ArrayList<>();

    }

    @Override
    public String toString() {
        return "Rule{" +
                "ruleName='" + ruleName + '\'' +
                ", alarmType='" + alarmType + '\'' +
                ", passes='" + passes + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactPhonenumber='" + contactPhonenumber + '\'' +
                ", entries=" + entries +
                '}';
    }

    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        try {
            json.put("rule_name", ruleName);
            // json.put("alarm_type", alarmType);
            json.put("contact_name", contactName);
            json.put("contact_phone", contactPhonenumber);
            json.put("passes", passes);

            for (AbstractRuleEntry r: entries) {
                json.accumulate("entries" , r.toJson());
            }
        } catch (JSONException e) {
            Log.d("com.avoidit", e.getMessage());
        }

        return json;
    }
    
}
