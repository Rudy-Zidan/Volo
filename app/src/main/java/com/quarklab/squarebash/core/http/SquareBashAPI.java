package com.quarklab.squarebash.core.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by rudy on 7/2/16.
 */
public class SquareBashAPI {
    private static AsyncHttpClient api = new AsyncHttpClient();
    private static String baseURI = "http://41.39.56.198:3000";

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
       String x = getAbsoluteUrl(url);
        api.get(x, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        api.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return (baseURI  + relativeUrl);
    }
}
