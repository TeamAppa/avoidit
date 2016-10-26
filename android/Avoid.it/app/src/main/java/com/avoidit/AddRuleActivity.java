package com.avoidit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class AddRuleActivity extends AppCompatActivity {
    private EditText ruleName;

    private Spinner entrySpinner;
    private ArrayAdapter<CharSequence> entryAdapter;
    private Button addNewEntryButton;

    private RadioButton radioButtonTextMessage;
    private RadioButton radioButtonAlarm;

    private EditText contactName;
    private EditText contactPhonenumber;

    private EditText numberOfPasses;
    private Button saveRuleButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rule);

        this.ruleName = (EditText) findViewById(R.id.ruleName);
        // Initiate spinners
        this.entrySpinner = (Spinner) findViewById(R.id.rule_spinner);
        this.addNewEntryButton = (Button) findViewById(R.id.add_new_entry_button);

        this.radioButtonTextMessage = (RadioButton) findViewById(R.id.radioButtonTextMessage);
        this.radioButtonAlarm = (RadioButton) findViewById(R.id.radioButtonAlarm);

        this.contactName = (EditText) findViewById(R.id.contactName);
        this.contactName = (EditText) findViewById(R.id.contactPhonenumber);

        this.numberOfPasses = (EditText) findViewById(R.id.numberOfPasses);

        this.saveRuleButton = (Button) findViewById(R.id.save_rule_button);

        // Create ArrayAdapters using the string array and a default spinner layout
        this.entryAdapter = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        this.entryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        this.entrySpinner.setAdapter(entryAdapter);

        // Set a standard value for number of passes
        this.numberOfPasses.setText("3");
    }
}
