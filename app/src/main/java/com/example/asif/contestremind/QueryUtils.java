package com.example.asif.contestremind;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by asif on 12/20/16.
 */

public final class QueryUtils {

    // Sample JSON response for a codeforces query
    private static final String SAMPLE_JSON_RESPONSE = "{\"status\":\"OK\",\"result\":[{\"id\":750,\"name\":\"Good Bye 2016\",\"type\":\"CF\",\"phase\":\"BEFORE\",\"startTimeSeconds\":1483106700},{\"id\":748,\"name\":\"Technocup 2017 - Elimination Round 3\",\"type\":\"CF\",\"phase\":\"BEFORE\",\"startTimeSeconds\":1482656700},{\"id\":747,\"name\":\"Codeforces Round #387 (Div. 2)\",\"type\":\"CF\",\"phase\":\"FINISHED\",\"startTimeSeconds\":1482656700},{\"id\":748,\"name\":\"Codeforces Round #388 (Div. 2)\",\"type\":\"CF\",\"phase\":\"FINISHED\",\"startTimeSeconds\":1482656700},{\"id\":748,\"name\":\"Codeforces Round #388 (Div. 2)\",\"type\":\"CF\",\"phase\":\"BEFORE\",\"startTimeSeconds\":1482656700},{\"id\":748,\"name\":\"Codeforces Round #388 (Div. 2)\",\"type\":\"CF\",\"phase\":\"BEFORE\",\"startTimeSeconds\":1482656700},{\"id\":748,\"name\":\"Codeforces Round #388 (Div. 2)\",\"type\":\"CF\",\"phase\":\"BEFORE\",\"startTimeSeconds\":1482656700},{\"id\":748,\"name\":\"Codeforces Round #388 (Div. 2)\",\"type\":\"CF\",\"phase\":\"BEFORE\",\"startTimeSeconds\":1482656700},{\"id\":748,\"name\":\"Codeforces Round #388 (Div. 2)\",\"type\":\"CF\",\"phase\":\"BEFORE\",\"startTimeSeconds\":1482656700},{\"id\":748,\"name\":\"Codeforces Round #388 (Div. 2)\",\"type\":\"CF\",\"phase\":\"BEFORE\",\"startTimeSeconds\":1482656700}]}";

    /**
     * This is a private constructor because no one should ever create a {@link QueryUtils} object
     */
    private QueryUtils(){

    }

    /**
     * Return a list of {@link Contest} objects that has been built up from parsing a JSON response.
     */
    public static ArrayList<Contest> extractContests(){

        ArrayList<Contest>contests = new ArrayList<>();


        // Try to parse the SAMPLE_JSON_RESPONSE. If there is a problem with parsing then
        // a JSONException exception object will be thrown.
        try{

            // Create root JSON object
            JSONObject baseJsonResponse = new JSONObject(SAMPLE_JSON_RESPONSE);

            // Create JSON array
            JSONArray contestArray = baseJsonResponse.getJSONArray("result");

            for(int i=0; i<contestArray.length();i++)
            {
                JSONObject currentContest = contestArray.getJSONObject(i);
                String status=currentContest.getString("phase");
                if(status.equals("BEFORE"))
                    status="Coming";
                else
                status="Ending";
                String name=currentContest.getString("name");
                long time=currentContest.getLong("startTimeSeconds")*1000;
                Contest contest = new Contest(status,name,time);
                contests.add(contest);
            }

        } catch (JSONException e){

            Log.e("QueryUtils", "Problem parsing the constest JSON results",e);
        }
        return  contests;
    }
}
