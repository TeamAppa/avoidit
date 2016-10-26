package com.avoidit;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ngraves3 on 10/26/16.
 */
public class CategoryRuleEntry extends AbstractRuleEntry<String> {

    public CategoryRuleEntry(int ruleId, String typeName, String displayName, String locationId) {
        super(ruleId, typeName, displayName);
        mAvoidanceCriteria = locationId;
    }

    @Override
    protected void jsonifyCriteria(JSONObject json, String avoidanceCriteria) throws JSONException {
        json.put("category_alias", mAvoidanceCriteria);
    }
}
