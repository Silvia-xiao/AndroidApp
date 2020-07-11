package com.example.androidass.networkconnection;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkConnection {
    private OkHttpClient client = null;
    private String results;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public NetworkConnection() {
        client = new OkHttpClient();
    }

    private static final String BASE_URL = "http://192.168.1.4:8080/movieMemoirs/webresources/restmem.";
    private static final String OMDB_name_URL = "http://www.omdbapi.com/?apikey=8376a1c3&s=";
    private static final String OMDB_id_URL = "http://www.omdbapi.com/?apikey=8376a1c3&i=";


    /**
     * Post data by different address passed in str to the server side data base.
     * @param str
     * @param obj
     * @return
     */
    public String postData(String str, Object obj) {
        //format the date.
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
        String json = gson.toJson(obj);
        String sResponse = "";
        //this is for testing, you can check how the json looks like in Logcat Log.i("json " , studentJson);
        Log.i("json", json);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + str)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            sResponse = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sResponse;
    }

    /**
     * Get data by different address from server side database.
     * @param str
     * @return
     */
    public String getData(String str) {
        final String methodPath = str;

        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * get movie detail from omdb api and find the information by using movie name.
     * @param movieName
     * @return
     */
    public String getMovieInfoFromOmdb(String movieName) {
        final String methodPath = movieName;

        Request.Builder builder = new Request.Builder();
        builder.url(OMDB_name_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * get movie detail from omdb api and find the information by using movie id.
     * @param movieId
     * @return
     */
    public String getMovieDetailInfoFromOmdb(String movieId) {
        final String methodPath = movieId;

        Request.Builder builder = new Request.Builder();
        builder.url(OMDB_id_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Post the cinema information to the server side database, no need to format the date.
     * @param str
     * @param obj
     * @return
     */
    public String postCinema(String str, Object obj) {
        Gson gson = new GsonBuilder().create();
        String personJson = gson.toJson(obj);
        String strResponse = "";
        //this is for testing, you can check how the json looks like in Logcat Log.i("json " , studentJson);
        Log.i("json", personJson);
        final String methodPath = str;
        RequestBody body = RequestBody.create(personJson, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + str)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            strResponse = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResponse;
    }



}