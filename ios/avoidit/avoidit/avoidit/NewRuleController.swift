//
//  NewRuleController.swift
//  avoidit
//
//  Created by Avery Dingler on 10/23/16.
//  Copyright © 2016 Avery Dingler. All rights reserved.
//
import UIKit

class NewRuleController: UIViewController {
    
    @IBOutlet var alertType: UIPickerView!
    
    @IBOutlet var ruleName: UITextField!
    override func viewWillAppear(_ animated: Bool) {
        self.navigationController?.setNavigationBarHidden(false, animated: false)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        //alertType.
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

    @IBAction func saveRule(_ sender: AnyObject) {
        ruleData.append(Rule(name: ruleName.text!, entries: [], numPasses: 3, contactName: "avery", contactNumber: "44949494"))
        DispatchQueue.main.async {
            self.performSegue(withIdentifier: "toMyRules", sender: self)
        }
    }
    
    //Calls this function when the tap is recognized.
    func dismissKeyboard() {
        //Causes the view (or one of its embedded text fields) to resign the first responder status.
        view.endEditing(true)
    }
    
    
}
