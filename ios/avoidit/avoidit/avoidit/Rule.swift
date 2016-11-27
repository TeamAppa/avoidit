//
//  Rule.swift
//  avoidit
//
//  Created by Avery Dingler on 10/26/16.
//  Copyright © 2016 Avery Dingler. All rights reserved.
//

import Foundation

var globalLat = ""

var globalLong = ""

var ruleData = [Rule]()

var ruleEntries = [RuleEntry]() //This is where rule entries are stored as a rule is created

var currentLocations = [Location]() //This is where location are stored after searching

var currentRule = Rule(id: "", name: "", entries: [], numPasses: 0, contactName: "", contactNumber: "", notificationType: "Text")

struct Rule {
    var id : String
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


struct Location {
    var id : String
    var name : String
    var address : String
    var zip : String
    var country : String
}

protocol RuleEntry {
    var ruleId : String {get set}
    var displayName : String {get set}
    var type : String {get set}
    func getJson() -> String
}



struct CategoryEntry : RuleEntry {
    internal var ruleId: String
    internal var displayName : String
    internal var categoryTitle : String
    var type = "CA"
    func getJson() -> String {
        return "{\"display_name\":\"\(displayName)\",\"category_title\":\"\(categoryTitle)\",\"type\":\"\(type)\"}"
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

func sendLocation() {
    print("SENDING LOCATION")
}

func searchLocations(searchTerm : String) {
    currentLocations = [Location]()
    
    var request = URLRequest(url: URL(string: "https://sheltered-scrubland-29626.herokuapp.com/avoiditapi/search")!)
    var token = UserDefaults.standard.value(forKey: "token") as! String
    token = "Token " + token
    request.setValue(token, forHTTPHeaderField: "Authorization")
    request.httpMethod = "POST"
    let body = "{\"latitude\":\"\(globalLat)\",\"longitude\":\"\(globalLong)\",\"term\":\"\(searchTerm)\"}"
    request.httpBody = body.data(using: .utf8)
    request.setValue("application/json; charset=utf-8", forHTTPHeaderField: "Content-Type")
    var done = false
    
    
    let task = URLSession.shared.dataTask(with: request) { data, response, error in
        guard let data = data, error == nil else {                                                 // check for fundamental networking error
            print("error=\(error)")
            return
        }
        let httpStatus = response as? HTTPURLResponse
        if (httpStatus?.statusCode != 200) {           // check for http errors
            debugPrint("statusCode should be 200, but is \(httpStatus?.statusCode)")
            debugPrint("response = \(response)")
        } else {
            //successfull login
            let responseString = String(data: data, encoding: .utf8)
            debugPrint("responseString = \(responseString)")
            
            do{
                
                let json = try JSONSerialization.jsonObject(with: data, options:.allowFragments)
                //debugPrint("JSON: " , json)
                for anItem in json as! Dictionary<String, AnyObject> {
                    //create a new rule
                    for location in anItem.value as! [Dictionary<String, AnyObject>] {
                        var newLocation = Location(id: "", name: "", address: "", zip: "", country: "")
                        print("=====NEW LOCATION=====")
                        print(location)
                        newLocation.name = location["name"]! as! String
                        newLocation.id = location["id"]! as! String
                        //get address out of location
                        let location2 = location["location"]!
                        newLocation.address = location2["address1"]!! as! String
                        newLocation.zip = location2["zip_code"]!! as! String
                        newLocation.country = location2["country"]!! as! String
                        currentLocations.append(newLocation)
                    }
                    
                }
                
            }catch {
                print("Error with Json: \(error)")
            }
            
            
        }
        //refresh the view controller here
        done = true
        
    }
    task.resume()
    while(!done) {
        //do nothing
    }

}

func loadRulesFromServer() {
    //clear the local rules
    ruleData = [Rule]()
    var request = URLRequest(url: URL(string: "https://sheltered-scrubland-29626.herokuapp.com/avoiditapi/getrules")!)
    var token = UserDefaults.standard.value(forKey: "token") as! String
    token = "Token " + token
    request.setValue(token, forHTTPHeaderField: "Authorization")
    request.httpMethod = "GET"
    request.setValue("application/json; charset=utf-8", forHTTPHeaderField: "Content-Type")
    var done = false
    
    
    let task = URLSession.shared.dataTask(with: request) { data, response, error in
        guard let data = data, error == nil else {                                                 // check for fundamental networking error
            print("error=\(error)")
            return
        }
        let httpStatus = response as? HTTPURLResponse
        if (httpStatus?.statusCode != 200) {           // check for http errors
            debugPrint("statusCode should be 200, but is \(httpStatus?.statusCode)")
            debugPrint("response = \(response)")
        } else {
            //successfull login
            let responseString = String(data: data, encoding: .utf8)
            debugPrint("responseString = \(responseString)")
            
            do{
                
                let json = try JSONSerialization.jsonObject(with: data, options:.allowFragments)
                debugPrint("JSON: " , json)
                //iterate through the rules in the JSON
                for anItem in json as! Dictionary<String, AnyObject> {
                    //create an empty rule
                    var newRule = Rule(id: "", name: "", entries: [], numPasses: 0, contactName: "", contactNumber: "", notificationType: "")
                    
                    newRule.id = anItem.key //set the id of the rule
                    //update the temporary rule with correct values
                    let ruleValues = (anItem.value as? [String: Any])!
                    newRule.numPasses = ruleValues["passes"] as! Int
                    newRule.contactName = ruleValues["contact_name"] as! String
                    newRule.contactNumber = ruleValues["contact_phone"] as! String
                    newRule.name = ruleValues["rule_name"] as! String
                    newRule.notificationType = "TEXT"
                    
                    //add the rule to rule data
                    ruleData.append(newRule)
                }
                
            }catch {
                print("Error with Json: \(error)")
            }
            
            
        }
        //refresh the view controller here
        done = true
        
    }
    task.resume()
    while(!done) {
        //do nothing
    }
    
    
    
}


