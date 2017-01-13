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
 * Created by asif on 1/13/17.
 */

public class DevSkillQueryUtils {
    /**
     * Return a lis to {@link Contest} objects.
     */
    public static List<Contest> fetchContestData(){
        String url="";
        String finalUrl="http://www.devskill.com";
        String date="";
        String name="";

        // Create an empty ArrayList that we can add contests
        List<Contest>contests=new ArrayList<>();

        // Try to parse the Html response string. If there is a problem with parsing then
        // an IOException exception object will be thrown.
        try{

            // Parse the page as DOM
            Document document = Jsoup.connect("http://www.devskill.com/Home").get();

            //Running Contest
            Elements running_elements =  document.getElementsByClass("widget running_contest_box");
            Elements running_contest_list = running_elements.get(0).getElementsByClass("contest_list");
            for(Element each_r_c_l:running_contest_list){
                Elements paragraph = each_r_c_l.getElementsByTag("p");
                for(Element each_paragraph: paragraph){
                    Elements aTags = each_paragraph.getElementsByTag("a");
                    for(Element each_aTag:aTags){
                        url = each_aTag.attr("href");
                        name =  each_aTag.text();
                    }
                    Elements st= each_paragraph.getElementsByTag("strong");
                    date =  st.get(0).text();
                    long timeInMillisecond = convertTimeInMillisecond(date);
                    Contest contest = new Contest("RUNNING",name,timeInMillisecond,finalUrl+url);
                    contests.add(contest);
                }
            }
            // Upcomming contest
            // Find the all elements by given class name
            Elements elements =document.getElementsByClass("widget upcoming_contest_box");
            Elements contest_list = elements.get(0).getElementsByClass("contest_list");
            for(Element each_contest_list:contest_list){
                Elements paragraph=each_contest_list.getElementsByTag("p");
                for(Element each_paragraph:paragraph){
                    Elements aTags =each_paragraph.getElementsByTag("a");
                    for(Element each_aTag:aTags){
                        url=each_aTag.attr("href");
                        name=each_aTag.text();
                    }
                    Elements st=each_paragraph.getElementsByTag("strong");
                    date=st.get(0).text();
                    long timeInMillisecond=convertTimeInMillisecond(date);
                    Contest contest = new Contest("UPCOMING",name,timeInMillisecond,finalUrl+url);
                    contests.add(contest);
                }
            }

        } catch (IOException e) {
            Log.e("DevSkillQueryUtils", "Problem parsing the contest JSOUP results",e);
        }

         return contests;

    }
    private static long convertTimeInMillisecond(String date){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy h:mm a");

        try{
            Date mDate = dateFormat.parse(date);
            long timeInMillisecond = mDate.getTime();
            return timeInMillisecond;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
