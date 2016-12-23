package com.example.asif.contestremind;

import android.content.AsyncTaskLoader;
import android.content.Context;

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
        List<Contest>contests = QueryUtils.fetchContestData(mUrl);
        return contests;
    }
}
