package com.example.asif.contestremind;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by asif on 12/19/16.
 */

public class ContestAdapter extends ArrayAdapter<Contest> {

    /**
     * Construct a new {@link ContestAdapter}.
     *
     * @param context of the app
     * @param contests is the list of contests, which is the data source of the adapter
     */
    public ContestAdapter(Context context, List<Contest>contests){

     super(context,0, contests);
    }

    /**
     * Return a list item view that displays information about the contest at the given position
     * in the list of contests
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if there is an existing list item view that we can reuse.
        // Otherwise if convertView is null then inflate a new list item layout.
        View listItemView = convertView;
        if(listItemView ==  null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.contest_list_item,parent,false);
        }

        // Find the contest at the given position in the list of contests
        Contest currentContest= getItem(position);



        // Find the ImageView with id image
        ImageView statusView = (ImageView) listItemView.findViewById(R.id.image);

        // Find the status of the contest
        String status=currentContest.getStatus();
        if(status.equals("RUNNING"))
            statusView.setImageResource(R.drawable.running);
        else
        statusView.setImageResource(R.drawable.upcoming);



        // Find the TextView with ID contest_name
        TextView nameView=(TextView) listItemView.findViewById(R.id.contest_name);
        // Display the contest name of the current contest in that TextView
        nameView.setText(currentContest.getName());

        // Create a new Date object from the time in millisecond of the contest
        Date dateObject = new Date(currentContest.getTimeInMillisecond()*1000);
        // Find the TextView with ID date
        TextView dateView=(TextView) listItemView.findViewById(R.id.date);

        // Format the date string (i.e "Mar 3, 1984")
        String formattedDate= formatDate(dateObject);
        // Display the date of the currnet contest in that TextView
        dateView.setText(formattedDate);

        // Find the TextView with view ID time
        TextView timeView=(TextView)listItemView.findViewById(R.id.time);
        // Format the time string(i.e "3.30 PM")
        String formattedTime = formatTime(dateObject);
        // Display the time of the current contest in that TextView
        timeView.setText(formattedTime);

        // Return the whole list item layout containing status,contest name ,date
        return  listItemView;
    }

    /**
     * Return date in ("LLL dd, yyyy") format
     */
    private String formatDate(Date dateObject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return time in ("h:mm a") format
     */
    private  String formatTime(Date dateObject){
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

}

