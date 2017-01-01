package com.example.asif.contestremind;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.content.AsyncTaskLoader;
//import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.widget.TextView;

public class ContestActivity extends AppCompatActivity implements LoaderCallbacks<List<Contest>> {

    private static final String LOG_TAG = ContestActivity.class.getName();

    // Url of the contest list
    public   String CONTEST_URL;
    // Adapter for the list of contest
    private ContestAdapter mAdapter;

    // TextView that is displayed when the list is empty
    private TextView mEmptyStateTextView;
    private static final int CONTEST_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);

        // Get the bundle that was send by MainActivity
        Bundle bundle = getIntent().getExtras();
        // Extract the data with the key value message

        CONTEST_URL = bundle.getString("message");
        // Find a reference to the link ListView in the layout
        ListView contestListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView)findViewById(R.id.empty_view);
        contestListView.setEmptyView(mEmptyStateTextView);

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


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if(networkInfo!=null && networkInfo.isConnected()){
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager=getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(CONTEST_LOADER_ID,null,this);
        }
        else{
            // Otherwise display an error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Set empty state view with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

      public Loader<List<Contest>> onCreateLoader(int i, Bundle bundle){

            return  new ContestLoader(this,CONTEST_URL);
        }
      public  void onLoadFinished(Loader<List<Contest>> loader,List<Contest> contests){

          // Hide loading indicator because the data has been loaded
          View loadingIndicator = findViewById(R.id.loading_indicator);
          loadingIndicator.setVisibility(View.GONE);
           // Set empty state text to display "No recent contest found."
          mEmptyStateTextView.setText(R.string.no_contests);
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
