//
//  Rule.swift
//  avoidit
//
//  Created by Avery Dingler on 10/26/16.
//  Copyright © 2016 Avery Dingler. All rights reserved.
//

import Foundation

var ruleData = [Rule]()

var ruleEntries = [RuleEntry]() //This is where rule entries are stored as a rule is created

var currentRule = Rule(name: "", entries: [], numPasses: 0, contactName: "", contactNumber: "", notificationType: "Text")

struct Rule {
    var name : String
    var entries = [RuleEntry]()
    var numPasses : Int
    var contactName : String
    var contactNumber : String
    var notificationType : String
    
    func getJson() -> String {
        return "{\"rule_name\":\"\(name)\",\"passes\":\"\(numPasses)\",\"entries\":\(jsonRuleEntryArray(array: entries)),\"contact_name\":\"\(contactName)\",\"contact_phone\":\"\(contactNumber)\"}"
    }
}

protocol RuleEntry {
    var ruleId : String {get set}
    var displayName : String {get set}
    var type : String {get set}
    func getJson() -> String
}

struct PriceEntry : RuleEntry {
    
    internal var ruleId: String
    internal var displayName : String
    var avoidPrices : [Int]
    internal var type = "PR"
    
    func getJson() -> String {
        return "{\"type\":\"\(type)\",\"display_name\":\"\(displayName)\",\"price_list\":\(priceJson())}"
    }
    
    func priceJson() -> String {
        var soln = "["
        var current = 1
        for price in avoidPrices {
            soln += "\"\(price)\""
            if (current != avoidPrices.count) {
                soln += ","
            }
            current += 1
        }
        soln += "]"
        return soln
    }
    
}



struct CategoryEntry : RuleEntry {
    internal var ruleId: String
    internal var displayName : String
    var categoryId : String
    var type = "CA"
    func getJson() -> String {
        return "{\"rule_id\":\"\(ruleId)\",\"type\":\"\(type)\",\"display_name\":\"\(displayName)\",\"category_alias\":\"\(categoryId)\"}"
    }
}

struct LocationEntry : RuleEntry {
    internal var ruleId: String
    internal var displayName : String
    var locationId : String
    var type = "LO"
    func getJson() -> String {
        return "{\"rule_id\":\"\(ruleId)\",\"type\":\"\(type)\",\"display_name\":\"\(displayName)\",\"location_id\":\"\(locationId)\"}"
    }
    
}


func initializeRules() {
    ruleData.append(Rule(name: "Avoid Ex-Girlfriends", entries: [], numPasses: 3, contactName: "avery", contactNumber: "44949494", notificationType: "Text"))
    ruleData.append(Rule(name: "Avoid Taco Bell", entries: [], numPasses: 3, contactName: "avery", contactNumber: "44949494", notificationType: "Text"))
    
    //get rules from the database and populate the var 'ruleData'
    //ruleEntries.append(LocationEntry(ruleId: "", displayName: "Avoid Fast Food", locationId: "3442"))
    //ruleEntries.append(LocationEntry(ruleId: "", displayName: "Avoid Coffee", locationId: "3442"))
    
}

func jsonRuleEntryArray(array : [RuleEntry]) -> String
{
    var soln = "["
    var count = 1

    for entry in array {
        soln += entry.getJson()
        if (count != array.count) {
            soln += ","
        }
        count += 1
        
    }
    soln += "]"
    return soln
}


