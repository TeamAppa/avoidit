package com.avoidit;

import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ngraves3 on 10/26/16.
 */
public class PriceRuleEntry extends AbstractRuleEntry<List<String>> {

    // int ruleId, String typeName, String displayName,
    public PriceRuleEntry(List<String> priceList) {
        super("PR","Avoid Price ()");

        this.setmDisplayName(beautifyDisplayName(priceList));
        mAvoidanceCriteria = priceList;
    }

    private String beautifyDisplayName(List<String> priceList){
        String displayName = "Avoid Price ";
        List<String> tempPriceList = new ArrayList<>();

        for (String s: priceList) {
            if (s.equals("1"))
                tempPriceList.add("$");
            if (s.equals("2"))
                tempPriceList.add("$$");
            if (s.equals("3"))
                tempPriceList.add("$$$");
        }

        return displayName + tempPriceList.toString().replace("[","(").replace("]",")");
    }

    @Override
    protected void jsonifyCriteria(JSONObject json, List<String> avoidanceCriteria) throws JSONException {
        json.put("price_list", new JSONArray(mAvoidanceCriteria));
    }

}
