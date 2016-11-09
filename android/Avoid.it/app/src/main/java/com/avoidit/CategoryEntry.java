package com.avoidit;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthon on 2016-11-08.
 */
public class CategoryEntry {
    private String title;
    private List<String> alias;

    public CategoryEntry(String title, JSONArray alias) {
        this.title = title;

        List<String> tempAlias = new ArrayList<>();
        for (int i= 0; i< alias.length();i++){
            try {
                tempAlias.add(alias.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.alias = tempAlias;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return title;
    }
}
