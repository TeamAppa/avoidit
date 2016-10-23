//
//  SecondViewController.swift
//  avoidit
//
//  Created by Avery Dingler on 10/2/16.
//  Copyright © 2016 Avery Dingler. All rights reserved.
//

import UIKit

class ProfileController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func logout(_ sender: AnyObject) {
        //clear the token and send a user to register page
        UserDefaults.standard.removeObject(forKey: "token")
        UserDefaults.standard.synchronize()
        DispatchQueue.main.async {
            self.performSegue(withIdentifier: "logout", sender: self)
        }
        
    }

}

