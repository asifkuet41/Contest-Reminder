package com.example.asif.contestremind;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asif on 12/28/16.
 */

public class TophQueryUtils /*extends AsyncTask<Void,Void,List<Contest>>*/ {

    public static  List<Contest> asif(){
        String url="";
        String finalUrl="https://toph.co";
        String name="";
        String []names=new String[100];
        String []urls = new String[100];
        String []times= new String[100];
        List<Contest>contests=new ArrayList<>();
        try{

            Document doc= Jsoup.connect("https://toph.co/").get();

            Elements elements=doc.getElementsByClass("portlet-body");
            int id=0;
            for(Element x:elements){
                Elements a =x.getElementsByTag("a");
                for(Element ab:a){
                    url=ab.attr("href");
                    name=ab.text();
                    names[id]=name;
                    urls[id]=finalUrl+url;
                    id++;
                    // Contest contest = new Contest(url,name);
                    // contests.add(contest);
                }
            }
            String time="";
            int id1=0;
            for(Element x:elements){
                Elements a=x.getElementsByTag("span");
                for(Element ab:a){
                    time=ab.attr("data-time");
                    if(time.length()!=0){
                        times[id1]=time;
                        id1++;
                    }

                }
            }

            for(int i=0;i<id;i++){
                Contest contest =  new Contest("upcoming",names[i],Long.parseLong(times[i])*1000,urls[i]);
                contests.add(contest);
            }

        } catch (IOException e){
            Log.e("ERROR", "Parsing Error");
        }
        return contests;
    }
    /*@Override
    public List<Contest> doInBackground(Void... voids) {
        String url="";
        String finalUrl="https://toph.co";
        String name="";
        String []names=new String[100];
        String []urls = new String[100];
        String []times= new String[100];
        List<Contest>contests=new ArrayList<>();
        try{

            Document doc= Jsoup.connect("https://toph.co/").get();

            Elements elements=doc.getElementsByClass("portlet-body");
            int id=0;
            for(Element x:elements){
                Elements a =x.getElementsByTag("a");
                for(Element ab:a){
                    url=ab.attr("href");
                    name=ab.text();
                    names[id]=name;
                    urls[id]=finalUrl+url;
                    id++;
                    // Contest contest = new Contest(url,name);
                    // contests.add(contest);
                }
            }
            String time="";
            int id1=0;
            for(Element x:elements){
                Elements a=x.getElementsByTag("span");
                for(Element ab:a){
                    time=ab.attr("data-time");
                    if(time.length()!=0){
                        times[id1]=time;
                        id1++;
                    }

                }
            }

            for(int i=0;i<id;i++){
                Contest contest =  new Contest("upcoming",names[i],Long.parseLong(times[i]),urls[i]);
                contests.add(contest);
            }

        } catch (IOException e){
            Log.e("ERROR", "Parsing Error");
        }
        return contests;
    }*/
}
