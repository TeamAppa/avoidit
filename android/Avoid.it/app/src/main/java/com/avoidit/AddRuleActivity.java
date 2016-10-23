package com.avoidit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddRuleActivity extends AppCompatActivity {
    Spinner entrySpinner;
    ArrayAdapter<CharSequence> entryAdapter;
    Spinner alertSpinner;
    ArrayAdapter<CharSequence> alertAdapter;
    Spinner contactSpinner;
    ArrayAdapter<CharSequence> contactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rule);

        this.entrySpinner = (Spinner) findViewById(R.id.rule_spinner);
        this.alertSpinner = (Spinner) findViewById(R.id.alert_spinner);
        this.contactSpinner = (Spinner) findViewById(R.id.contacts_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        this.entryAdapter = ArrayAdapter.createFromResource(this,
                R.array.example_entries, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        this.entryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.entrySpinner.setAdapter(entryAdapter);

        this.alertAdapter = ArrayAdapter.createFromResource(this,
                R.array.alert_types, android.R.layout.simple_spinner_item);
        this.alertAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.alertSpinner.setAdapter(alertAdapter);

        this.contactsAdapter = ArrayAdapter.createFromResource(this,
                R.array.example_contacts, android.R.layout.simple_spinner_item);
        this.contactsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.contactSpinner.setAdapter(contactsAdapter);
    }
}
