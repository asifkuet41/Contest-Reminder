package com.example.asif.contestremind;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class ContestActivity extends AppCompatActivity {

    public static final String LOG_TAG = ContestActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);

        // Create a fake list of contest.
        ArrayList<Contest> contests = QueryUtils.extractContests();

        // Find a reference to the link ListView in the layout
        ListView contestListView = (ListView) findViewById(R.id.list);

        // Create a new Adapter that take the list of contest as input.
        ContestAdapter adapter = new ContestAdapter(this, contests);


        // Set the adapter on the ListView so the list can be populated in the user interface
        contestListView.setAdapter(adapter);
    }
}
