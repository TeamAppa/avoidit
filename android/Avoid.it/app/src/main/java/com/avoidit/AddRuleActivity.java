package com.avoidit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.List;

public class AddRuleActivity extends AppCompatActivity {
    private EditText mRuleName;

    private Button mLocationEntryButton;
    private Button mCategoryEntryButton;
    private Button mPriceEntryButton;

    private RadioButton mTextMessageRadioButton;
    private RadioButton mAlarmRadioButton;

    private EditText mContactName;
    private EditText mContactPhonenumber;

    private EditText mNumberOfPasses;
    private Button mSaveRuleButton;


    private void initiateComponents(){
        this.mRuleName = (EditText) findViewById(R.id.ruleName);

        this.mLocationEntryButton = (Button) findViewById(R.id.location_entry_button);
        this.mCategoryEntryButton = (Button) findViewById(R.id.category_entry_button);
        this.mPriceEntryButton = (Button) findViewById(R.id.price_entry_button);

        this.mTextMessageRadioButton = (RadioButton) findViewById(R.id.radioButtonTextMessage);
        this.mAlarmRadioButton = (RadioButton) findViewById(R.id.radioButtonAlarm);

        this.mContactName = (EditText) findViewById(R.id.contactName);
        this.mContactPhonenumber = (EditText) findViewById(R.id.contactPhonenumber);

        this.mNumberOfPasses = (EditText) findViewById(R.id.numberOfPasses);

        this.mSaveRuleButton = (Button) findViewById(R.id.save_rule_button);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rule);
        this.initiateComponents();

        this.mLocationEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LocationEntryActivity.class);
                startActivity(intent);
            }
        });
        this.mCategoryEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CategoryEntryActivity.class);
                startActivity(intent);
            }
        });
        this.mPriceEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PriceEntryActivity.class);
                startActivity(intent);
            }
        });

        // Set a standard value for number of passes
        this.mNumberOfPasses.setText("3");

        this.mSaveRuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
                finish();
            }
        });
    }

    private void checkFields(){
        // Get current rules
        List rules = RuleTempHolder.getRules();
        Rule rule = RuleTempHolder.getLastRule();

        String ruleName = mRuleName.getText().toString();
        String alarmType = this.getAlarmType();
        String numberOfPasses = mNumberOfPasses.getText().toString();
        String contactName = mContactName.getText().toString();
        String contactPhonenumber = mContactPhonenumber.getText().toString();
        rule.ruleName = ruleName;
        rule.alarmType = alarmType;
        rule.passes = numberOfPasses;
        rule.contactName = contactName;
        rule.contactPhonenumber = contactPhonenumber;

        System.out.println(RuleTempHolder.getLastRule().toJson());
        // System.out.println(RuleTempHolder.getLastRule().entries.get(0).toJson().toString());
    }

    private String getAlarmType(){
        String alarmType = "";
        if (mTextMessageRadioButton.isChecked()){
            alarmType = "text";
        } else if (mAlarmRadioButton.isChecked()){
        }
        return alarmType;
    }

}
