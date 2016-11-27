//
//  category.swift
//  avoidit
//
//  Created by Avery Dingler on 11/7/16.
//  Copyright © 2016 Avery Dingler. All rights reserved.
//

import Foundation

var categories = [String]()

func loadCategoriesFromServer() {
    categories = [String]()
    var request = URLRequest(url: URL(string: "https://sheltered-scrubland-29626.herokuapp.com/avoiditapi/getcategoriesjson")!)
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
            //successfull grab of categories
            let responseString = String(data: data, encoding: .utf8)
            debugPrint("responseString = \(responseString)")
            
            do{
                
                let json = try JSONSerialization.jsonObject(with: data, options:.allowFragments)
                debugPrint("JSON: " , json)
                for anItem in json as! [Dictionary<String, AnyObject>] {
                    
                    let title = anItem["title"]
                    categories.append(title as! String)
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

