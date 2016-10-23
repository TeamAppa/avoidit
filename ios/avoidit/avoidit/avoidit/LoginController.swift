//
//  RegisterController.swift
//  avoidit
//
//  Created by Avery Dingler on 10/2/16.
//  Copyright © 2016 Avery Dingler. All rights reserved.
//

import UIKit

class LoginController: UIViewController {
    
    @IBOutlet var email: UITextField!
    @IBOutlet var password: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func attemptLogin(_ sender: UIButton) {
        if (email.text!.isEmpty || password.text!.isEmpty) {
            //information is missing
            let phoneAlert = UIAlertController(title: "Login Error", message: "Please enter a email and password", preferredStyle: UIAlertControllerStyle.alert)
            
            phoneAlert.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.default, handler: nil))
            
            self.present(phoneAlert, animated: true, completion: nil)
        } else {
            //attempt to login
            let (success, message) = loginPost()
            if (success) {
                DispatchQueue.main.async {
                    self.performSegue(withIdentifier: "successfulLogin", sender: self)
                }
            } else {
                let loginError = UIAlertController(title: "Error Loging In", message: message, preferredStyle: UIAlertControllerStyle.alert)
                
                loginError.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.default, handler: nil))
                
                self.present(loginError, animated: true, completion: nil)
            }
            
        }
    }
    
    func loginPost() -> (Bool, String) {
        var request = URLRequest(url: URL(string: "https://sheltered-scrubland-29626.herokuapp.com/avoiditapi/gettoken")!)
        var success = true
        var message = ""
        var complete = false
        request.httpMethod = "POST"
        var jsonString = "{\"username\":\"" + email.text! + "\","
        
        jsonString += "\"password\":\"" + password.text! + "\"}"
        
        print("BEFORE POST: " + jsonString)
        request.httpBody = jsonString.data(using: .utf8)
        request.setValue("application/json; charset=utf-8", forHTTPHeaderField: "Content-Type")
        
        
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            guard let data = data, error == nil else {                                                 // check for fundamental networking error
                print("error=\(error)")
                return
            }
            let httpStatus = response as? HTTPURLResponse
            if (httpStatus?.statusCode != 200) {           // check for http errors
                debugPrint("statusCode should be 200, but is \(httpStatus?.statusCode)")
                debugPrint("response = \(response)")
                message = "invalid credentials"
                success = false
                
            } else {
                //successfull login
                let responseString = String(data: data, encoding: .utf8)
                debugPrint("responseString = \(responseString)")
                
                do{
                    
                    let json = try JSONSerialization.jsonObject(with: data, options:.allowFragments)
                    debugPrint("JSON: " , json)
                    let dictionary = json as? [String: String]
                    debugPrint("DICTIONARY: ", dictionary)
                    if let token = dictionary!["token"] {
                        print("token: " + token)
                        success = true
                        //save token to defaults
                        UserDefaults.standard.set(token, forKey: "token")
                        UserDefaults.standard.synchronize()
                    } else {
                        success = false
                        message = "Error getting token. Contact Avoid.it"
                    }
                    
                }catch {
                    print("Error with Json: \(error)")
                }
            
            
            }
            complete = true
        }
        task.resume()
        while (!complete) {
            //do nothing
            //this is here so the http request fully completes before returning
        }
        
        return (success, message)
    }
    
}
