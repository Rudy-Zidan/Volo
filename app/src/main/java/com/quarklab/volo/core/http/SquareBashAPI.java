package com.quarklab.volo.core.http;


import android.annotation.SuppressLint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by rudy on 7/2/16.
 */
public class SquareBashAPI {
    private enum requestType {POST,GET}
    //private static String baseURI = "http://game.quarklabs.co";
    private static String baseURI = "http://192.168.1.3:3000";
    private static final String USER_AGENT = "Mozilla/5.0";

    @SuppressLint("NewApi")
    public static JSONArray get(String path, String params) {
        JSONArray result = null;
        try {
            result = new JSONArray(call(path,requestType.GET,params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result ;
    }

    public static JSONObject postObject(String path, String params){
        JSONObject result = null;
        try {
            result = new JSONObject(call(path,requestType.POST,params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result ;
    }

    public static JSONArray post(String path, String params){
        JSONArray result = null;
        try {
            result = new JSONArray(call(path,requestType.POST,params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result ;
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return (baseURI  + relativeUrl);
    }

    private static String call(String uri,requestType method, String rawData){
        String response = "";
        try {
            URL url = new URL(getAbsoluteUrl(uri));
            //create the connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method.name());
            connection.setRequestProperty("User-Agent", USER_AGENT);
            if(connection.getRequestMethod().equals(requestType.POST.name())){
                connection.setDoOutput(true);
                connection.setRequestProperty( "Content-Type", "application/json" );
                connection.setRequestProperty( "Content-Length", String.valueOf(rawData.length()));
                OutputStreamWriter osr = new OutputStreamWriter(connection.getOutputStream());
                osr.write(rawData);
                osr.flush();
                osr.close();
            }
            String line = "";
            //create your inputsream
            InputStreamReader isr = new InputStreamReader(
                    connection.getInputStream());
            //read in the data from input stream, this can be done a variety of ways
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            //get the string version of the response data
            response = sb.toString();

            isr.close();
            reader.close();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
