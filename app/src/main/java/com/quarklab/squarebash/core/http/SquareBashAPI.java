package com.quarklab.squarebash.core.http;


import android.annotation.SuppressLint;
import android.os.Build;

import org.apache.http.HttpResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by rudy on 7/2/16.
 */
public class SquareBashAPI {
    private static enum requestType {POST,GET};
    private static String baseURI = "http://41.39.56.198:3000";
    private static final String USER_AGENT = "Mozilla/5.0";

    @SuppressLint("NewApi")
    public static JSONArray get(String path, String params) {
        JSONArray result = null;
        try {
            result = new JSONArray(call(path,requestType.GET));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result ;
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return (baseURI  + relativeUrl);
    }

    private static String call(String uri,requestType method){
        String response = "";
        try {
            URL url = null;
            url = new URL(getAbsoluteUrl(uri));

            //create the connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method.name());
            connection.setRequestProperty("User-Agent", USER_AGENT);
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
