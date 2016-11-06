package com.avoidit;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthon on 2016-11-06.
 */

public class RuleTempHolder {
    public static List<Rule> rules;

    public RuleTempHolder() {
        this.rules = new ArrayList<Rule>();
    }

    public RuleTempHolder(List<Rule> rules) {
        this.rules = rules;
    }

    public static List<Rule> getRules() {
        return rules;
    }

    public static Rule getLastRule(){
        return rules.get(rules.size() - 1);
    }

    @Override
    public String toString() {
        String retString = "";
        for (Rule r: this.rules) {
            retString += r.toString() + "\n";
        }

        return "RuleTempHolder\n" + retString;
    }
}
