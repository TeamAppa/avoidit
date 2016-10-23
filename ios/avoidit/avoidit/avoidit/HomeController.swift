//
//  FirstViewController.swift
//  avoidit
//
//  Created by Avery Dingler on 10/2/16.
//  Copyright © 2016 Avery Dingler. All rights reserved.
//

import UIKit

class HomeController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        //print current user token
        if let token = UserDefaults.standard.value(forKey: "token") {
            print("CURRENT USER TOKEN: ")
            print(token)
        }
    }
    
    
    override func viewWillAppear(_ animated: Bool) {
        //trying to hide the navigation bar
        self.navigationController?.setNavigationBarHidden(true, animated: false)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

