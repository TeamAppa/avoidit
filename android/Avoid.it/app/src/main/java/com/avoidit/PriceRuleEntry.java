package com.avoidit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ngraves3 on 10/26/16.
 */
public class PriceRuleEntry extends AbstractRuleEntry<List<Integer>> {

    public PriceRuleEntry(int ruleId, String typeName, String displayName, List<Integer> priceList) {
        super(ruleId, typeName, displayName);
        mAvoidanceCriteria = priceList;
    }

    @Override
    protected void jsonifyCriteria(JSONObject json, List<Integer> avoidanceCriteria) throws JSONException {
        json.put("price_list", new JSONArray(mAvoidanceCriteria));
    }
}
