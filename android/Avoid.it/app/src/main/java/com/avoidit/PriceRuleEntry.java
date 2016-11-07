package com.avoidit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ngraves3 on 10/26/16.
 */
public class PriceRuleEntry extends AbstractRuleEntry<List<String>> {

    // int ruleId, String typeName, String displayName,
    public PriceRuleEntry(List<String> priceList) {
        super("PR","Avoid Price");
        mAvoidanceCriteria = priceList;
    }

    @Override
    protected void jsonifyCriteria(JSONObject json, List<String> avoidanceCriteria) throws JSONException {
        json.put("price_list", new JSONArray(mAvoidanceCriteria));
    }


}
