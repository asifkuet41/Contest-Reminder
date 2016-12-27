package com.example.asif.contestremind;

import android.app.LoaderManager;
import android.content.Intent;
import android.net.Uri;
import android.content.AsyncTaskLoader;
//import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;

public class ContestActivity extends AppCompatActivity implements LoaderCallbacks<List<Contest>> {

    private static final String LOG_TAG = ContestActivity.class.getName();

    // Url of the contest list
    private  static final String CONTEST_URL="https://toph.co/";
    // Adapter for the list of contest
    private ContestAdapter mAdapter;

    private static final int CONTEST_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(CONTEST_LOADER_ID,null,this);

        // Find a reference to the link ListView in the layout
        ListView contestListView = (ListView) findViewById(R.id.list);

        // Create a new Adapter that takes an empty list of contests as input.
        mAdapter = new ContestAdapter(this,new ArrayList<Contest>());


        // Set the adapter on the ListView so the list can be populated in the user interface
        contestListView.setAdapter(mAdapter);

       contestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // Find the current contest that was clicked on
                Contest currentContest = mAdapter.getItem(position);

                // Convert the String Url into URI object
                Uri contestUri = Uri.parse(currentContest.getUrl());

                // Create a new intent to view the contest URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,contestUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }

      public Loader<List<Contest>> onCreateLoader(int i, Bundle bundle){
            return  new ContestLoader(this,CONTEST_URL);
        }
      public  void onLoadFinished(Loader<List<Contest>> loader,List<Contest> contests){
          mAdapter.clear();
          if(contests!=null&&!contests.isEmpty()){
              mAdapter.addAll(contests);
          }
    }

    @Override
    public void onLoaderReset(Loader<List<Contest>> loader) {
        mAdapter.clear();
    }

}
