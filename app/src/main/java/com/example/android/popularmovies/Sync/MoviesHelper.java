package com.example.android.popularmovies.Sync;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by AGWU SMART ELEZUO on 5/10/2017.
 */

public class MoviesHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "movies.db";
    private static final int DB_VERSION = 1;

    public MoviesHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_DATABASE = "CREATE TABLE " + MoviesContract.MoviesEntry.TABLE_NAME + " ( " +
                MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_ID +" INTEGER NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_RATING + " TEXT NOT NULL " + ");";

        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
