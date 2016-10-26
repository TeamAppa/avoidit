package com.avoidit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddRuleActivity extends AppCompatActivity {
    private Spinner entrySpinner;
    private ArrayAdapter<CharSequence> entryAdapter;
    private Spinner alertSpinner;
    private ArrayAdapter<CharSequence> alertAdapter;
    
    private EditText ruleName;
    private EditText numberOfPasses;

    private Button addNewEntry;
    private Button addNewContactButton;
    private Button saveRuleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rule);
        // Initiate spinners
        this.entrySpinner = (Spinner) findViewById(R.id.rule_spinner);
        this.alertSpinner = (Spinner) findViewById(R.id.alert_spinner);
        this.ruleName = (EditText) findViewById(R.id.ruleName);
        this.numberOfPasses = (EditText) findViewById(R.id.numberOfPasses);
        this.addNewEntry = (Button) findViewById(R.id.add_new_entry_button);
        this.saveRuleButton = (Button) findViewById(R.id.save_rule_button);

        // Create ArrayAdapters using the string array and a default spinner layout
        this.entryAdapter = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item);
        this.alertAdapter = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        this.entryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.alertAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        this.entrySpinner.setAdapter(entryAdapter);
        this.alertSpinner.setAdapter(alertAdapter);

        // Set a standard value for number of passes
        this.numberOfPasses.setText("3");
    }
}
