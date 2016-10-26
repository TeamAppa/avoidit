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

        this.radioButtonTextMessage = (RadioButton) findViewById(R.id.radioButtonTextMessage);
        this.radioButtonAlarm = (RadioButton) findViewById(R.id.radioButtonAlarm);

        this.contactName = (EditText) findViewById(R.id.contactName);
        this.contactName = (EditText) findViewById(R.id.contactPhonenumber);

        this.numberOfPasses = (EditText) findViewById(R.id.numberOfPasses);

        this.saveRuleButton = (Button) findViewById(R.id.save_rule_button);

        // Set a standard value for number of passes
        this.numberOfPasses.setText("3");
    }
}
