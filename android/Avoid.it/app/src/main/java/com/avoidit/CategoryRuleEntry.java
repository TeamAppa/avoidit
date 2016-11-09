package com.avoidit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ngraves3 on 10/26/16.
 */
public class CategoryRuleEntry extends AbstractRuleEntry<String> {

    public CategoryRuleEntry(int ruleId, String typeName, String displayName, String alias) {
        super(ruleId, typeName, displayName);
        mAvoidanceCriteria = alias;
    }

    public CategoryRuleEntry(String displayName, String alias) {
        super("CA", displayName);
        mAvoidanceCriteria = alias;
    }

    @Override
    protected void jsonifyCriteria(JSONObject json, String avoidanceCriteria) throws JSONException {
        json.put("category_title", mDisplayName);
    }
}
