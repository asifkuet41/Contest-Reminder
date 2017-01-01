package com.example.asif.contestremind;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by asif on 12/30/16.
 */

public class CodechefQueryUtils {
    private static final String LOG_TAG = CodechefQueryUtils.class.getSimpleName();
      private static final String CODECHEF_URL= "https://www.codechef.com/";


    public static List<Contest> fetchContestData(){


        // Create an empty ArrayList for storing URL of the contest
        ArrayList<String> contestUrl = new ArrayList<>();

        // Create an empty ArrayList for storing NAME of the contest
        ArrayList<String> contestName = new ArrayList<>();

        // Create an empty ArrayList for storing TIME of the contest
        ArrayList<String> contestTime =  new ArrayList<>();

        // Create an empty ArrayList fo storing STATUS of the contest
        ArrayList<String> contestStatus = new ArrayList<>();

        // Create a List so that we can add contest
        List<Contest>contests = new ArrayList<>();
        try{

            // Parse the page as DOM
            Document document = Jsoup.connect("https://www.codechef.com/contests").get();

            // Get all elements that has class dataTable
            Elements dataTables = document.getElementsByClass("dataTable");

            // Variable for checking number of table
            int countTable=0;

            // Extract data from each table of dataTables
            for(Element dataTable:dataTables){

                 // If countTable is equal to 0 then contests are running
                  if(countTable == 0) {
                      // Get all elements of the tbody Tag.
                      Elements tableBodys = dataTable.getElementsByTag("tbody");
                      // Extract data from each tableBody.
                      for (Element tableBody : tableBodys) {
                          //Get all elements of the Tag tr
                          Elements tableRows = tableBody.getElementsByTag("tr");
                          // Get the values fo tableColumn for each tableRow
                          for (Element tableRow : tableRows) {
                              Elements tableColumns = tableRow.getElementsByTag("td");

                                  contestUrl.add(tableColumns.get(0).text());
                                  contestName.add(tableColumns.get(1).text());
                                  contestTime.add(tableColumns.get(2).text());
                                  contestStatus.add("RUNNING");
                              }
                          }
                      }
                  // If countTable is equal to 1 then contests are upcoming
                  else if(countTable==1){
                      // Get all elements of the tbody Tag.
                      Elements tableBodys = dataTable.getElementsByTag("tbody");
                      // Extract data from each tableBody.
                      for (Element tableBody : tableBodys) {
                          //Get all elements of the Tag tr
                          Elements tableRows = tableBody.getElementsByTag("tr");
                          // Get the values fo tableColumn for each tableRow
                          for (Element tableRow : tableRows) {
                              Elements tableColumns = tableRow.getElementsByTag("td");

                              contestUrl.add(tableColumns.get(0).text());
                              contestName.add(tableColumns.get(1).text());
                              contestTime.add(tableColumns.get(2).text());
                              contestStatus.add("UPCOMING");
                          }
                      }
                  }
                countTable++;
            }
            for(int i=0;i<contestName.size();i++){
                long time = getTimeInMillisecond(contestTime.get(i));
                Contest contest = new Contest(contestStatus.get(i),contestName.get(i),time,CODECHEF_URL+contestUrl.get(i));
                contests.add(contest);
            }
        } catch (IOException e){
            Log.e("CodeChefQueryUtils", "Problem parsing the contest JSOUP results",e);
        }
        return contests;
    }

    /**
     * Return time in millisecond
     */
    private static  long getTimeInMillisecond(String time){
        long timeInMillisecond = 0;
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try{
            Date mDate = timeFormat.parse(time);
            timeInMillisecond = mDate.getTime();
        } catch (ParseException e){
            e.printStackTrace();
        }
        // Convert time to Bangladesh time
        return timeInMillisecond + 1800000;
    }
}
