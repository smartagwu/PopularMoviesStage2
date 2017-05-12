package com.example.android.popularmovies.Utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by AGWU SMART ELEZUO on 4/8/2017.
 */

public class NetworkUtils {

    public static final String URL_FOR_POPULAR_MOVIES = "http://api.themoviedb.org/3/movie/popular";
    public static final String URL_FOR_TOP_RATED_MOVIES = "http://api.themoviedb.org/3/movie/top_rated";
    public static final String API_KEY = "api_key";
    public static final String POPULAR_MOVIES_ID = "popular";
    public static final String TOP_RATED_MOVIES_ID = "top rated";

    //Enter your themoviedb.com api key here
    public static final String API_KEY_VALUE = "**Enter API Key here**";
    private static Uri uri;

    public static URL buildUrl(String category){
        if (category == POPULAR_MOVIES_ID){
            uri = Uri.parse(URL_FOR_POPULAR_MOVIES).buildUpon()
                    .appendQueryParameter(API_KEY, API_KEY_VALUE)
                    .build();
        }else if(category == TOP_RATED_MOVIES_ID){
            uri = Uri.parse(URL_FOR_TOP_RATED_MOVIES).buildUpon()
                    .appendQueryParameter(API_KEY, API_KEY_VALUE)
                    .build();
        }

        URL url = null;
        try {
            url = new URL(uri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


}
