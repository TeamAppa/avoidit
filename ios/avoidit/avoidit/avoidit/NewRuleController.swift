//
//  NewRuleController.swift
//  avoidit
//
//  Created by Avery Dingler on 10/23/16.
//  Copyright © 2016 Avery Dingler. All rights reserved.
//
import UIKit

class NewRuleController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource {
    
    @IBOutlet var alertType: UIPickerView!
    
    var alertDataSource = ["Text", "Alarm"]
    
    override func viewWillAppear(_ animated: Bool) {
        self.navigationController?.setNavigationBarHidden(false, animated: false)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        //alertType.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return alertDataSource.count
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return alertDataSource[row]
    }
    
    
    
    
    
    
    
}
