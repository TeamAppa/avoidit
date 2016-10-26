//
//  Rule.swift
//  avoidit
//
//  Created by Avery Dingler on 10/26/16.
//  Copyright © 2016 Avery Dingler. All rights reserved.
//

import Foundation

var ruleData = [Rule]()

struct Rule {
    var name : String
    var entries = [RuleEntry]()
    var numPasses : Int
    var contactName : String
    var contactNumber : String
}

protocol RuleEntry {

}

struct PriceEntry : RuleEntry {
    var displayName : String
    var avoidPrices : [Int]
}

struct CategoryEntry : RuleEntry {
    var displayName : String
    var categoryId : String
    
}

struct LocationEntry : RuleEntry {
    var displayName : String
    var locationId : String
    
}


func initializeRules() {
    ruleData.append(Rule(name: "Avoid Ex-Girlfriends", entries: [], numPasses: 3, contactName: "avery", contactNumber: "44949494"))
    ruleData.append(Rule(name: "Avoid Pricey Food", entries: [], numPasses: 3, contactName: "avery", contactNumber: "44949494"))
    
}
