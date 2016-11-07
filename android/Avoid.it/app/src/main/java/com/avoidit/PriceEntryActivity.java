package com.avoidit;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

public class PriceEntryActivity extends AppCompatActivity {
    private Switch mOneDollar;
    private Switch mTwoDollar;
    private Switch mThreeDollar;
    private Button mAddPriceEntryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_entry);

        this.mOneDollar = (Switch) findViewById(R.id.one_dollar_switch);
        this.mTwoDollar = (Switch) findViewById(R.id.two_dollar_switch);
        this.mThreeDollar = (Switch) findViewById(R.id.three_dollar_switch);
        this.mAddPriceEntryButton = (Button) findViewById(R.id.add_price_entry_button);




        this.mAddPriceEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptAddPriceCritera(v);
            }
        });

    }

    private void attemptAddPriceCritera(View v){
        Rule rule = RuleContainer.getLastRule();
        List<String> priceList = new ArrayList<>();
        boolean cancel = false;

        if (mOneDollar.isChecked()){
            priceList.add("1");
        }
        if (mTwoDollar.isChecked()){
            priceList.add("2");
        }
        if (mThreeDollar.isChecked()){
            priceList.add("3");
        }

        if (priceList.isEmpty()){
            cancel = true;
        }

        if (cancel) {
            Snackbar.make(v,"Please select at least one price criteria.",Snackbar.LENGTH_SHORT).show();
        } else {
            rule.entries.add(new PriceRuleEntry(priceList));
            finish();
        }
    }
}