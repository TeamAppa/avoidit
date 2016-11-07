package com.avoidit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import org.json.JSONObject;

import java.util.LinkedList;
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

    private EditText mPasses;
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

        this.mPasses = (EditText) findViewById(R.id.numberOfPasses);

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
        this.mPasses.setText("3");

        this.mSaveRuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptAddRule(v);
            }
        });
    }

    private void attemptAddRule(View v){
        // Get current rules
        List rules = RuleContainer.getRules();
        Rule rule = RuleContainer.getLastRule();

        String ruleName = mRuleName.getText().toString();
        String alarmType = this.getAlarmType();
        String numberOfPasses = mPasses.getText().toString();
        String contactName = mContactName.getText().toString();
        String contactPhonenumber = mContactPhonenumber.getText().toString();

        boolean cancel = false;
        List<View> focusView = new LinkedList<>();

        if (TextUtils.isEmpty(ruleName)){
            mRuleName.setError("Rule name is required");
            focusView.add(mRuleName);
            cancel = true;
        }

        if (TextUtils.isEmpty(numberOfPasses)){
            mPasses.setError("Number of passes is required");
            focusView.add(mPasses);
            cancel = true;
        }

        if (TextUtils.isEmpty(contactName)){
            mContactName.setError("Contact name is required");
            focusView.add(mContactName);
            cancel = true;
        }

        if (TextUtils.isEmpty(contactPhonenumber)){
            mContactPhonenumber.setError("Contact phonenumber is required");
            focusView.add(mContactPhonenumber);
            cancel = true;
        }

        if (rule.entries.isEmpty()) {
            Snackbar.make(v,"Please add at least 1 entry.",Snackbar.LENGTH_SHORT).show();
            cancel = true;
        }

        if (cancel){
            for (View fv : focusView) {
                fv.requestFocus();
            }
        } else {
            rule.ruleName = ruleName;
            rule.alarmType = alarmType;
            rule.passes = numberOfPasses;
            rule.contactName = contactName;
            rule.contactPhonenumber = contactPhonenumber;

            // Used for debugging
            System.out.println(RuleContainer.getLastRule().toJson());
            attemptPostRule(RuleContainer.getLastRule().toJson());
            finish();
        }
    }

    private String getAlarmType(){
        String alarmType = "";
        if (mTextMessageRadioButton.isChecked()){
            alarmType = "text";
        } else if (mAlarmRadioButton.isChecked()){
        }
        return alarmType;
    }

    private void attemptPostRule(JSONObject json){
        HttpHelper helper = new HttpHelper();
        final String ENDPOINT = "/createrulewithentries";

        SharedPreferences settings = getSharedPreferences(RegistrationActivity.PREFS_NAME, 0);
        String token = settings.getString("token","null");
        if (token != "null"){
            helper.postJson(ENDPOINT,json,token);
        }
    }

}
