//
//  NewRuleController.swift
//  avoidit
//
//  Created by Avery Dingler on 10/23/16.
//  Copyright © 2016 Avery Dingler. All rights reserved.
//
import UIKit

class NewRuleController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    @IBOutlet var numberOfPasses: UILabel!
    @IBOutlet var contactPhone: UITextField!
    @IBOutlet var alertType: UISegmentedControl!
    @IBOutlet var stepper: UIStepper!
    @IBOutlet var ruleEntriesTableView: UITableView!
    @IBOutlet var ruleName: UITextField!
    override func viewWillAppear(_ animated: Bool) {
        self.navigationController?.setNavigationBarHidden(true, animated: false)
        
        if (currentRule.name != "") {
            ruleName.text = currentRule.name
        }
        numberOfPasses.text = String(currentRule.numPasses)
        stepper.value = Double(currentRule.numPasses)
        
        //print rule entry json for debugging
        
        
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
        if (ruleEntries.count == 0 || (ruleName.text?.isEmpty)!) {
            var errorMessage = ""
            if (ruleName.text?.isEmpty)! {
                errorMessage = "You must enter a rule name"
            } else {
                errorMessage = "You must have at least one rule entry"
            }
            //throw error because nothing is selected
            let addRuleError = UIAlertController(title: "Error Adding Rule", message: errorMessage, preferredStyle: UIAlertControllerStyle.alert)
            
            addRuleError.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.default, handler: nil))
            
            self.present(addRuleError, animated: true, completion: nil)
            
        } else {
            currentRule.contactName = "placeholder"
            currentRule.contactNumber = contactPhone.text!
            currentRule.name = ruleName.text!
            currentRule.notificationType = alertType.titleForSegment(at: alertType.selectedSegmentIndex) ?? "Text"
            currentRule.entries = ruleEntries
            //ruleData.append(currentRule) //change this with a post to database
            let (success, message) = postRule()
            if (success) {
                //print json for debugging purposes
                print("JSON PRINTING")
                print(currentRule.getJson())
                
                //clear out rule that was created
                ruleEntries = [RuleEntry]() //This is where rule entries are stored as a rule is created
                currentRule = Rule(id: "", name: "", entries: [], numPasses: 0, contactName: "", contactNumber: "", notificationType: "Text")
                loadRulesFromServer()
                DispatchQueue.main.async {
                    self.performSegue(withIdentifier: "toMyRules", sender: self)
                }
            } else {
                let addRuleError = UIAlertController(title: "Error Adding Rule", message: message, preferredStyle: UIAlertControllerStyle.alert)
                
                addRuleError.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.default, handler: nil))
                
                self.present(addRuleError, animated: true, completion: nil)
            }
            
        }
    }
    
    // MARK: - Table view data source
    
    func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return ruleEntries.count
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Rule Entry Cell", for: indexPath)
        
        // Configure the cell...
        cell.textLabel?.text = ruleEntries[indexPath.row].displayName
        
        return cell
    }
    
    @IBAction func stepper(_ sender: UIStepper) {
        let value = sender.value
        
        numberOfPasses.text = String(Int(value))
        currentRule.numPasses = Int(value)
    }
    //Calls this function when the tap is recognized.
    func dismissKeyboard() {
        //Causes the view (or one of its embedded text fields) to resign the first responder status.
        view.endEditing(true)
    }
    
    
    @IBAction func saveName(_ sender: UITextField) {
        currentRule.name = ruleName.text!
    }
    
    @IBAction func alertTypeChanged(_ sender: Any) {
        currentRule.notificationType = alertType.titleForSegment(at: alertType.selectedSegmentIndex) ?? "Text"
        
    }
    
    func postRule() -> (Bool, String) {
        var request = URLRequest(url: URL(string: "https://sheltered-scrubland-29626.herokuapp.com/avoiditapi/createrulewithentries")!)
        var token = UserDefaults.standard.value(forKey: "token") as! String
        token = "Token " + token
        request.setValue(token, forHTTPHeaderField: "Authorization")
        
        var success = true
        var message = ""
        var complete = false
        request.httpMethod = "POST"
        let jsonString = currentRule.getJson()
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
                message = "Error posting your rule. Please try again."
                success = false
                
            } else {
                //successfull posting of rule
                let responseString = String(data: data, encoding: .utf8)
                debugPrint("responseString = \(responseString)")
                
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
