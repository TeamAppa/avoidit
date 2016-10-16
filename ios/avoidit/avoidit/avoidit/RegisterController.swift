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
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
            self.performSegue(withIdentifier: "successfulRegister", sender: self)
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

}
