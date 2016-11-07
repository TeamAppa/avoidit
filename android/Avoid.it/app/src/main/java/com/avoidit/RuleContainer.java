package com.avoidit;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthon on 2016-11-06.
 */

public class RuleContainer {

    private static RuleContainer instance = new RuleContainer();

    private List<Rule> rules;

    private RuleContainer() {
        rules = new ArrayList<>();
    }

    public void addRule(Rule r) {
        rules.add(r);
    }

    public final List<Rule> getRules() {
        return rules;
    }

    public Rule getLastRule(){
        return rules.get(rules.size() - 1);
    }

    @Override
    public String toString() {
        String retString = "";
        for (Rule r: rules) {
            retString += r.toString() + "\n";
        }
        return "RuleContainer\n" + retString;
    }

    public static RuleContainer getInstance() {
        return instance;
    }
}
