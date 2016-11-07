package com.avoidit;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 * Created by ngraves3 on 10/26/16.
 */
public abstract class AbstractRuleEntry<T> {

    private int mRuleId;
    private String mTypeName;
    private String mDisplayName;
    protected T mAvoidanceCriteria;

    public AbstractRuleEntry() {
    }

    public AbstractRuleEntry(int ruleId, String typeName, String displayName) {
        mRuleId = ruleId;
        mTypeName = typeName;
        mDisplayName = displayName;
    }

    public AbstractRuleEntry(String typeName, String displayName) {
        this.mTypeName = typeName;
        this.mDisplayName = displayName;
    }

    public void setRuleId(int ruleId) {
        mRuleId = ruleId;
    }

    public void setmDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    /**
     * Gets the type name of this avoidable target.
     * @return the type name of the avoidable target.
     */
    public String getTypeName() {
        return mTypeName;
    }

    /**
     * Returns the avoidance criteria for this target.
     * @return the avoidance criteria.
     */
    public T getCriteria() {
        return mAvoidanceCriteria;
    }

    /**
     * Returns a JSON representation of this avoidance target.
     * @return
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            // json.put("rule_id", mRuleId);
            json.put("display_name", mDisplayName);
            json.put("type", mTypeName);

            jsonifyCriteria(json, mAvoidanceCriteria);

        } catch (JSONException e) {
            Log.d("com.avoidit", e.getMessage());
        }

        return json;
    }

    /**
     * This method adds the appropriate avoidance criteria to the json accumulator object.
     * @param json
     * @param avoidanceCriteria
     */
    protected abstract void jsonifyCriteria(JSONObject json, T avoidanceCriteria) throws JSONException;
}
