package com.example.asif.contestremind;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private  Integer [] iconId = {
            R.drawable.codeforces,
            R.drawable.codechef,
            R.drawable.toph,
            R.drawable.devskill,
            R.drawable.logo2

    };
    private String [] contestUrl={
            "http://codeforces.com/api/contest.list?gym=false",
            "https://www.codechef.com/",
            "https://toph.co/",
            "http://www.devskill.com/Home",
            "https://atcoder.jp/contest"

    };
    // Adapter for the list of contest site
    private MainAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       mAdapter = new MainAdapter(this,iconId);

        // Find a reference to the link ListView in the layout
        ListView contest_site_view = (ListView)findViewById(R.id.main_list);
        contest_site_view.setAdapter(mAdapter);

        contest_site_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {



                // Create a new intent to pass the contest site to ContestActivity
                Intent intent = new Intent(MainActivity.this,ContestActivity.class);

                // Store the iconId with the key value message to send the ContestActivity
                intent.putExtra("message",contestUrl[position]);

                // Send the intent to launch a new activity
                startActivity(intent);

            }
        });

    }
}
