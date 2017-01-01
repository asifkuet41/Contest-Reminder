package com.example.asif.contestremind;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asif on 12/23/16.
 */

public class ContestLoader extends AsyncTaskLoader<List<Contest>> {

      private  static final  String LOG_TAG = ContestLoader.class.getSimpleName();
    private  String mUrl;
    public ContestLoader(Context context,String url){
        super(context);
        mUrl=url;
    }
    protected void onStartLoading(){

        forceLoad();
    }
    @Override
    public List<Contest> loadInBackground() {

       if (mUrl == null){
           return null;
       }
        List<Contest>contests = new ArrayList<>();
       if (mUrl.equals("https://toph.co/")){
          contests = TophQueryUtils.asif();

       }
        else if (mUrl.equals("https://www.codechef.com/")){
           contests= CodechefQueryUtils. fetchContestData();
       }
        else
           contests = CodeforcesQueryUtils.fetchContestData(mUrl);

        return contests;
    }
}
