package com.example.asif.contestremind;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 * Created by asif on 1/13/17.
 */

public class AtCoderQueryUtils {
    /**
     * Return a lis to {@link Contest} objects.
     */
    public static List<Contest> fetchContestData(){
        // Create an empty ArrayList so that we can add contest status
        ArrayList<String>mStatus =  new ArrayList<>();
        // Creat an empty ArrayList so that we can add contest name
        ArrayList<String>mName =  new ArrayList<>();
        // Create an empty ArrayList so that we can add contest url
        ArrayList<String>mUrl = new ArrayList<>();
        // Creat an empty ArrayList so that we can add contest time
        ArrayList<String>mTime = new ArrayList<>();

        // Create an empty List so that we can add Contest obeject
        List<Contest> contests = new ArrayList<>();

        // Try to parse the Html response string. If there is a problem with parsing then
        // an IOException exception object will be thrown.
        try{

            Document document = Jsoup.connect("https://atcoder.jp/contest").get();

            Elements dataTable = document.getElementsByClass("table table-default table-striped table-hover table-condensed table-bordered");

            // Extract Running Contest
            Elements r_body = dataTable.get(0).getElementsByTag("tbody");
            Elements r_raw = r_body.get(0).getElementsByTag("tr");
            for(int i=0;i<r_raw.size();i=i+3){
                Elements columns= r_raw.get(i).getElementsByTag("td");
                mStatus.add("RUNNING");
                mTime.add(columns.get(0).getElementsByTag("a").text());
                mName.add(columns.get(1).getElementsByTag("a").text());
                mUrl.add(columns.get(1).getElementsByTag("a").attr("href"));
            }
            // Extract Upcoming Contest
            Elements u_body = dataTable.get(1).getElementsByTag("tbody");
            Elements u_raw = u_body.get(0).getElementsByTag("tr");
            for(int i=0;i<u_raw.size();i+=3){
                Elements columns= u_raw.get(i).getElementsByTag("td");
                mStatus.add("UPCOMING");
                mTime.add(columns.get(0).getElementsByTag("a").text());
                mName.add(columns.get(1).getElementsByTag("a").text());
                mUrl.add(columns.get(1).getElementsByTag("a").attr("href"));
            }

            for(int i=0;i<mName.size();i++){
                long timeInMillisecond = getTimeInMillisecond(mTime.get(i));
                Contest contest = new Contest(mStatus.get(i),mName.get(i),timeInMillisecond,mUrl.get(i));
                contests.add(contest);
            }
        } catch (IOException e) {
            Log.e("AtCoderQueryUtils", "Problem parsing the contest JSOUP results",e);
        }
        return contests;
    }
    private static  long getTimeInMillisecond(String time){
        long timeInMillisecond = 0;
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        try{
            Date mDate = timeFormat.parse(time);
            timeInMillisecond = mDate.getTime();
        } catch (ParseException e){
            e.printStackTrace();
        }
        // Convert time to Bangladesh time
        return timeInMillisecond;
    }
}
