//
//  PriceEntryController.swift
//  avoidit
//
//  Created by Avery Dingler on 10/26/16.
//  Copyright © 2016 Avery Dingler. All rights reserved.
//

import UIKit

class PriceEntryController: UIViewController {
    
    
    @IBOutlet var one: UISwitch!
    @IBOutlet var two: UISwitch!
    @IBOutlet var three: UISwitch!
    
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
    
    @IBAction func addEntry(_ sender: Any) {
        if (!one.isOn && !two.isOn && !three.isOn) {
            //throw error because nothing is selected
            let addEntryError = UIAlertController(title: "Error Adding Entry", message: "At least one price must be selected", preferredStyle: UIAlertControllerStyle.alert)
            
            addEntryError.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.default, handler: nil))
            
            self.present(addEntryError, animated: true, completion: nil)
        } else {
            var input = [Int]()
            var display = ""
            if (one.isOn) {
                input.append(1)
                display += "$"
            }
            if (two.isOn) {
                input.append(2)
                if (display != "") {
                    display += ", "
                }
                display += "$$"
            }
            if (three.isOn) {
                input.append(3)
                if (display != "") {
                    display += ", "
                }
                display += "$$$"
            }
            ruleEntries.append(PriceEntry(ruleId: "", displayName: "Avoid Price (" + display + ")", avoidPrices: input, type: "PR"))
            
            //go back to new rule page
            DispatchQueue.main.async {
                self.performSegue(withIdentifier: "priceEntryToNewRule", sender: self)
            }
            
            
        }
    }
    
    
    //Calls this function when the tap is recognized.
    func dismissKeyboard() {
        //Causes the view (or one of its embedded text fields) to resign the first responder status.
        view.endEditing(true)
    }
}
