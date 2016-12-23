package com.example.asif.contestremind;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by asif on 12/20/16.
 */

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

           public static   String SINGLE_CONTEST_URL ="http://codeforces.com/contest/";
            public static String ALL_CONTEST_URL = "http://codeforces.com/contests";



    /**
     * Return a lis to {@link Contest} objects.
     * @param requestUrl as a String
     *
     */

    public static List<Contest> fetchContestData(String requestUrl){

        // Create Url object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG,"Problem with making the HTTP request.",e);
        }
        // Extract jsonResponse and create a list of {@link Contest}
        List<Contest> contests = extractContests(jsonResponse);

        // Return the list of {@link Contest}
        return contests;

    }

    /**
     * This is a private constructor because no one should ever create a {@link QueryUtils} object
     */
    private QueryUtils(){

    }
    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl){
        URL url=null;

        try{
            url = new URL(stringUrl);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG,"Problem building the URL",e);
        }
        return  url;
    }
    private static List<Contest> extractContests(String contestJSON){

        // Check th JSON string is empty or not
        if (TextUtils.isEmpty(contestJSON)){
            return  null;
        }

        // Create an empty ArrayList that we can add contests
        List<Contest> contests = new ArrayList<>();



        // Try to parse the JSON response string. If there is a problem with parsing then
        // a JSONException exception object will be thrown.
        try{

            JSONObject baseJsonResponse= new JSONObject(contestJSON);
            JSONArray earthquakeArray=baseJsonResponse.getJSONArray("result");
            for(int i=0;i<earthquakeArray.length();i++){
                String finalStatus="";
                StringBuilder url = new StringBuilder();
                JSONObject current = earthquakeArray.getJSONObject(i);
               String contestId = current.getString("id");
                String location = current.getString("name");
                long time = current.getLong("startTimeSeconds")*1000;
                String status=current.getString("phase");
                if(status.equals("BEFORE"))
                {
                    url = url.append(ALL_CONTEST_URL);
                    finalStatus="UPCOMING";
                }

                else if(status.equals("FINISHED"))
                  break;
                else {
                    finalStatus="RUNNING";
                    url = url.append(SINGLE_CONTEST_URL).append(contestId);
                }
                String finalUrl = url.toString();

                Contest contest =new Contest(finalStatus,location,time,finalUrl);
                contests.add(contest);

            }


        } catch (JSONException e){

            Log.e("QueryUtils", "Problem parsing the contest JSON results",e);
        }
        return  contests;
    }
    /**
     *  Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse="";

        // If the URL is null then return early.
        if (url == null)
        {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Check the request was successful or not
            if (urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse  = readFromStream(inputStream);
            }
            else{
                Log.e(LOG_TAG, "Error response code: "+ urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG,"Problem with retrieving the contest JSON results",e);
        } finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();;
            }
        }
        return  jsonResponse;

    }
    /**
     *  Converts the {@link InputStream} into a String which contains the whole JSON response from the server
     */
    private static String readFromStream(InputStream inputStream)throws IOException{
        StringBuilder output= new StringBuilder();

        if (inputStream!=null)
        {
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();
            while(line!=null){
                output.append(line);
                line=reader.readLine();
            }
        }
        return output.toString();
    }
    /**
     * Return a list of {@link Contest} objects that has been built up from parsing a JSON response.
     */

}
