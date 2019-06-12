package com.example.sunshine.quakereport;
import android.content.Context;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.widget.TextView;

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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {
    public static String geturl;
    public static final String LOG_TAG ="hi";


    /** Sample JSON response for a USGS query */
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static ArrayList<information> Fetch_earthquake(String x)
    {
       try{ Thread.sleep(1000);}
       catch (InterruptedException e)
       {
           e.printStackTrace();
       }
        String connect=null;
        ArrayList<information>data;
        URL url=createUrl(x);
        try {
             connect=EstablishConnection(url);

        }
        catch (IOException e)
        {
            Log.v("EstablishConnection","error in establishing connection");

        }
        data=extractEarthquakes(connect);

        return data;


    }
    private static String EstablishConnection(URL url) throws IOException
    {
        String JsonfORMAT="";
        if(url==null)
        {
            return JsonfORMAT;
        }
        HttpURLConnection connect=null;
        InputStream inputStream=null;
        try
        {
             connect= (HttpURLConnection) url.openConnection();
            connect.setReadTimeout(10000 /* milliseconds */);
            connect.setConnectTimeout(15000 /* milliseconds */);
             connect.setRequestMethod("GET");
             connect.connect();
             if(connect.getResponseCode()==200)
             {
                 inputStream=connect.getInputStream();
                 JsonfORMAT=readFromStream(inputStream);


             }
             else
             {
                 Log.e(LOG_TAG, "Error response code: " + connect.getResponseCode());

             }
        }
        catch (IOException e)
        {
            Log.v("readFromStream","error in reading from input stream");

        }
        finally {
            if (connect != null) {
                connect.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return JsonfORMAT;

    }
    private static String readFromStream(InputStream inputStream) throws IOException
    {

        StringBuilder x=new StringBuilder();
        if(inputStream==null)
        {
            return null;
        }

          InputStreamReader  read_from_input=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader readline = new BufferedReader(read_from_input);
            String read=readline.readLine();
            while (read!=null)
            {
                x.append(read);
                read=readline.readLine();
            }



        return x.toString();

    }

    private static URL createUrl(String url)
    {
        URL Url=null;
      try
      {
           Url=new URL(url);
      }
      catch (MalformedURLException e)
      {
          Log.v("Url error exception","invaild format for url");
      }
      return Url;
    }

    /**
     * Return a list of {@link information} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<information> extractEarthquakes(String JsonFormat) {
        // Create an empty ArrayList that we can start adding earthquakes to
        if (TextUtils.isEmpty(JsonFormat)) {
            return null;
        }
        ArrayList<information> earthquakes = new ArrayList<>();
        SimpleDateFormat mdateFormat=new SimpleDateFormat("MMM dd,YYYY\n\t hh:mm a");
        DecimalFormat decimalFormat=new DecimalFormat("0.0");

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject root=new JSONObject(JsonFormat);
            JSONArray feature=root.getJSONArray("features");
            for(int i=0;i<feature.length();i++)
            {
                JSONObject getObjectOfFeatures=feature.getJSONObject(i);
                JSONObject properties=getObjectOfFeatures.getJSONObject("properties");
                geturl=properties.getString("url");
                double mag=properties.getDouble("mag");
                String place=properties.getString("place");
                long time=properties.getLong("time");
                String[]placeholder;
                if(place.toLowerCase().contains("of"))
                {
                    placeholder=place.toLowerCase().split("of");
                    earthquakes.add(new information(decimalFormat.format(mag),placeholder[0]+"oF\n",placeholder[1],mdateFormat.format(new Date(time))));

                }
                else{

                    earthquakes.add(new information(decimalFormat.format(mag),"Near Of\n",place,mdateFormat.format(new Date(time))));


                }



            }



        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}
