//
//  CategoryEntryController.swift
//  avoidit
//
//  Created by Avery Dingler on 10/26/16.
//  Copyright © 2016 Avery Dingler. All rights reserved.
//

import UIKit

class CategoryEntryController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    
    @IBOutlet var categoryTableView: UITableView!
    
    override func viewWillAppear(_ animated: Bool) {
        self.navigationController?.setNavigationBarHidden(false, animated: false)
        //load the categries from the server
        if (categories.count == 0) {
            loadCategoriesFromServer()
        }
        
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
    
    // MARK: - Table view data source
    
    func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return categories.count
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Category Cell", for: indexPath)
        
        // Configure the cell...
        cell.textLabel?.text = categories[indexPath.row]
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("selected cell \(indexPath.row)")
        let selectedCategory = categories[indexPath.row]
        print("SELECTED: " + selectedCategory)
        ruleEntries.append(CategoryEntry(ruleId: "", displayName: "Avoid " + selectedCategory, categoryTitle: selectedCategory, type: "CA"))
        
        //go back to new rule page
        DispatchQueue.main.async {
            self.performSegue(withIdentifier: "categoryEntryToNewRule", sender: self)
        }
        
        
    }
    
}
