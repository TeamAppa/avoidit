package com.avoidit;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthon on 2016-11-06.
 */

class RuleContainer {
    static List<Rule> rules;

    RuleContainer() {
        rules = new ArrayList<Rule>();
    }

    public RuleContainer(List<Rule> rules) {
        RuleContainer.rules = rules;
    }

    static List<Rule> getRules() {
        return rules;
    }

    static Rule getLastRule(){
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
}
