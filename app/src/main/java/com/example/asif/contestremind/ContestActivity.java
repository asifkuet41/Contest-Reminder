package com.example.asif.contestremind;

import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
//import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

                final int  itemPosition = position;

                //Create a new Alert Dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ContestActivity.this);

                // Set the dialog message
                alertDialog.setMessage("Do you want to notification for this contest ?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Find the current contest that was clicked on
                                Contest currentContest = mAdapter.getItem(itemPosition);

                                // Convert the String Url into URI object
                                Uri contestUri = Uri.parse(currentContest.getUrl());

                                // add the contest name into the DONE_ALARM list
                                SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                                editor.putInt(currentContest.getName(),1);
                                editor.commit();

                                // Create Calendar object.
                                Calendar calendar= Calendar.getInstance();

                                //Get the contest time from Contest Class
                                calendar.setTimeInMillis(currentContest.getTimeInMillisecond());
                                // Extract year from the time
                                int year = calendar.get(Calendar.YEAR);
                                // Extract month from the time
                                int month = calendar.get(Calendar.MONTH);
                                // Extract date from the time
                                int  date =calendar.get(Calendar.DAY_OF_MONTH);
                                // Extract hour from the time
                                int  hour= calendar.get(Calendar.HOUR_OF_DAY);
                                // Extract minute from the time
                                int minute = calendar.get(Calendar.MINUTE);


                                // Now we need to set the time for notification
                                // Set year
                                calendar.set(Calendar.YEAR,2017);
                                // Set month
                                calendar.set(Calendar.MONTH,0);
                                // Set date
                                calendar.set(Calendar.DAY_OF_MONTH,16);
                                // Set hour before one hour of the contest
                                int final_hour;
                                if(hour==0)
                                     final_hour=23;
                                else
                                final_hour=hour-1;
                                calendar.set(Calendar.HOUR_OF_DAY,2);
                                // Set minute
                                calendar.set(Calendar.MINUTE,37);

                                // Create a new Intent to view the AlertReceiver Class
                                Intent intent = new Intent(getApplicationContext(),AlertReceiver.class);

                                SharedPreferences preferences=getSharedPreferences("RequestCode",MODE_PRIVATE);
                                int count = preferences.getInt("codeValue",0);

                                Bundle notiId = new Bundle();
                                notiId.putInt("notification_id",count);
                                intent.putExtras(notiId);

                                String  notificationMessage ="";

                                if (CONTEST_URL.equals("https://toph.co/")){
                                    notificationMessage="You have a new contest at toph";

                                }
                                else if (CONTEST_URL.equals("https://www.codechef.com/")){
                                    notificationMessage="You have a new contest at Codechef";
                                }
                                else if(CONTEST_URL.equals("http://www.devskill.com/Home")){
                                    notificationMessage="You have a new contest at Devskill";
                                }
                                else if(CONTEST_URL.equals("https://atcoder.jp/contest")){
                                    notificationMessage="You have a new contest at AtCoder";
                                }
                                else
                                    notificationMessage="You have a new contest Codeforces";

                                Bundle bundle = new Bundle();
                                bundle.putString("ContestSite",notificationMessage);
                                intent.putExtras(bundle);

                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),count,intent,PendingIntent.FLAG_ONE_SHOT);
                                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                                alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                                preferences.edit().putInt("codeValue",count+1).commit();

                               // Create a new intent to view the contest URI
                                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,contestUri);

                                // Send the intent to launch a new activity
                                startActivity(websiteIntent);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                // Find the current contest that was clicked on
                                Contest currentContest = mAdapter.getItem(itemPosition);

                                // Convert the String Url into URI object
                                Uri contestUri = Uri.parse(currentContest.getUrl());

                                // Create a new intent to view the contest URI
                                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,contestUri);

                                // Send the intent to launch a new activity
                                startActivity(websiteIntent);
                            }
                        });
                Contest currentContest = mAdapter.getItem(position);

                if(currentContest.getStatus().equals("UPCOMING")) {
                    SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                    int value = prefs.getInt(currentContest.getName(),0);
                    if(value==0){
                        AlertDialog alert = alertDialog.create();
                        alert.setTitle("Contest Reminder");
                        alert.show();
                    }
                    else{
                        // Convert the String Url into URI object
                        Uri contestUri = Uri.parse(currentContest.getUrl());

                        // Create a new intent to view the contest URI
                        Intent websiteIntent = new Intent(Intent.ACTION_VIEW,contestUri);

                        // Send the intent to launch a new activity
                        startActivity(websiteIntent);
                    }

                }
                else{

                    // Convert the String Url into URI object
                    Uri contestUri = Uri.parse(currentContest.getUrl());

                    // Create a new intent to view the contest URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW,contestUri);

                    // Send the intent to launch a new activity
                    startActivity(websiteIntent);
                }

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
