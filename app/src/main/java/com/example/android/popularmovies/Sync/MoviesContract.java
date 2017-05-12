package com.example.android.popularmovies.Sync;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URL;

/**
 * Created by AGWU SMART ELEZUO on 5/10/2017.
 */

public class MoviesContract {

    public static final String AUTHORITY = "com.example.android.popularmovies";
    public static final Uri BASE_URL = Uri.parse("content://" + AUTHORITY);
    public static final String PATH = "movies";

    public static class MoviesEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_URL.buildUpon().appendPath(PATH).build();

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_POSTER = "poster";
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_ID = "id";
    }
}
