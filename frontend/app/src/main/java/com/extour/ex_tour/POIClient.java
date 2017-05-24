package com.extour.ex_tour;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class POIClient {

    private static final String baseAPIUrl = "http://10.0.2.2:9000/api/";
    private AsyncHttpClient client;

    public POIClient(){

        this.client = new AsyncHttpClient();
    }

    public String getApiUrl(String relativeUrl) {

        return baseAPIUrl + relativeUrl;
    }

    public void getPOI(String id, JsonHttpResponseHandler handler) {

        String url = getApiUrl("poi?id=");
        client.get(url + id, handler);
    }

    public void getTagsPOI(String search, JsonHttpResponseHandler handler) {

        String url = getApiUrl("pois?tag=");
        client.get(url + search, handler);
    }
}