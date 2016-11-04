//
//  RegisterController.swift
//  avoidit
//
//  Created by Avery Dingler on 10/2/16.
//  Copyright © 2016 Avery Dingler. All rights reserved.
//

import UIKit

class RegisterController: UIViewController {
    
    @IBOutlet var name: UITextField!
    @IBOutlet var phone: UITextField!
    @IBOutlet var email: UITextField!
    @IBOutlet var passwordConfirm: UITextField!
    @IBOutlet var password: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
        //Looks for single or multiple taps.
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(RegisterController.dismissKeyboard))
        
        //Uncomment the line below if you want the tap not not interfere and cancel other interactions.
        //tap.cancelsTouchesInView = false
        
        view.addGestureRecognizer(tap)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        //extract token and see if user is already logged in
        if let token = UserDefaults.standard.value(forKey: "token") {
            print("TOKEN ON APP LOAD: ")
            print(token)
            DispatchQueue.main.async {
                self.performSegue(withIdentifier: "successfulRegister", sender: self)
            }
        } else {
            print("user needs to login")
        }
       
        
    }
    

    
    @IBAction func verifyRegistration(_ sender: UIButton) {
        if (!isValidEmail(testStr: email.text!)) {
            //invalid email. display an alert
            let emailAlert = UIAlertController(title: "Invalid Email", message: "Please enter a valid email", preferredStyle: UIAlertControllerStyle.alert)
            
            emailAlert.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.default, handler: nil))
            
            self.present(emailAlert, animated: true, completion: nil)
            
        } else if (!validPassword(password: password.text!, password2: passwordConfirm.text!)){
            //passwords do not match
            let passwordAlert = UIAlertController(title: "Passwords do not match", message: "Please try again", preferredStyle: UIAlertControllerStyle.alert)
            
            passwordAlert.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.default, handler: nil))
            
            self.present(passwordAlert, animated: true, completion: nil)
        } else if (!validName(name: name.text!)) {
            //invalid name
            let nameAlert = UIAlertController(title: "You must enter a name", message: "Please try again", preferredStyle: UIAlertControllerStyle.alert)
            
            nameAlert.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.default, handler: nil))
            
            self.present(nameAlert, animated: true, completion: nil)
        } else if (!validPhone(value: phone.text!)) {
            //invalid phone
            let phoneAlert = UIAlertController(title: "Invalid phone number", message: "Please enter a valid number", preferredStyle: UIAlertControllerStyle.alert)
            
            phoneAlert.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.default, handler: nil))
            
            self.present(phoneAlert, animated: true, completion: nil)
        } else {
            //attempt a post
            let (success, message) = postAttempt()
            if (!success) {
                let registerError = UIAlertController(title: "Error Registering", message: message, preferredStyle: UIAlertControllerStyle.alert)
                
                registerError.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.default, handler: nil))
                
                self.present(registerError, animated: true, completion: nil)
            } else {
                print("SUCCESSFUL REGISTRATION")
                DispatchQueue.main.async {
                    self.performSegue(withIdentifier: "successfulRegister", sender: self)
                }
                
            }
        }
        
    }
    
    func isValidEmail(testStr:String) -> Bool {
        // print("validate calendar: \(testStr)")
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"
        
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        return emailTest.evaluate(with: testStr)
    }
    
    func validPassword(password:String, password2:String) -> Bool {
        let same = password.isEqual(password2)
        let notEmpty = !password.isEmpty
        return same && notEmpty
        
    }
    
    func validName(name:String) -> Bool {
        return !name.isEmpty
    }
    
    func validPhone(value: String) -> Bool {
        return !value.isEmpty
    }
    
    //Calls this function when the tap is recognized.
    func dismissKeyboard() {
        //Causes the view (or one of its embedded text fields) to resign the first responder status.
        view.endEditing(true)
    }
    
    func postAttempt() -> (Bool, String){
        var request = URLRequest(url: URL(string: "https://sheltered-scrubland-29626.herokuapp.com/avoiditapi/createuser")!)
        var success = true
        var message = ""
        request.httpMethod = "POST"
        var complete = false
        var jsonString = "{\"first_name\":\"" + name.text! + "\","
        
        jsonString += "\"phone\":\"" + phone.text! + "\","
        
        jsonString += "\"password\":\"" + password.text! + "\","
        
        jsonString += "\"email\":\"" + email.text! + "\"}"
        
        print("BEFORE POST: " + jsonString)
        
        request.httpBody = jsonString.data(using: .utf8)
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            guard let data = data, error == nil else {                                                 // check for fundamental networking error
                print("error=\(error)")
                return
            }
            let httpStatus = response as? HTTPURLResponse
            if (httpStatus?.statusCode != 200) {           // check for http errors
                print("statusCode should be 200, but is \(httpStatus?.statusCode)")
                print("response = \(response)")
                success = false
                message = "email and password must be unique"
                
            } else {
                //determine what to do with a response
                let responseString = String(data: data, encoding: .utf8)
                print("responseString = \(responseString)")
                
                do{
                    
                    let json = try JSONSerialization.jsonObject(with: data, options:.allowFragments)
                    print("printing json")
                    print(json)
                    let dictionary = json as? [String: Any]
                    print("DICTIONARY")
                    print(dictionary ?? "ERROR")
                    let messageArr = dictionary!["message"] as? NSArray
                    print(messageArr!)
                    print("------")
                    print(messageArr?[0] ?? "ERROR")
                    if((messageArr?[0] as! String).contains("Account created")) {
                        //success full creation of account
                        let token = dictionary?["token"]
                        print(token ?? "ERROR")
                        UserDefaults.standard.set(token, forKey: "token")
                        UserDefaults.standard.synchronize()
                        message = messageArr?[0] as! String
                        
                    } else {
                        //acount creation failure
                        print("ERROR");
                        success = false
                        message = messageArr?[0] as! String
                        
            
                    }
                    
                }catch {
                    print("Error with Json: \(error)")
                }
       
            }
            
            complete = true;
            
            
        }
        task.resume()
        while (!complete) {
            //do nothing
        }
        return (success, message)
        
        
    }

}
