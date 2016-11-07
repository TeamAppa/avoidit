package com.avoidit;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class CategoryEntryActivity extends AppCompatActivity {

    private ListView mCategoriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_entry);

        mCategoriesList = (ListView) findViewById(R.id.categories_listview);
    }

    public class GetCategoriesTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {
            return null;
        }
    }
}


