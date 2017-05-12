package com.example.android.popularmovies.Sync;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by AGWU SMART ELEZUO on 5/10/2017.
 */

public class MoviesProvider extends ContentProvider {

    private static final int DIRECTORY_CODE = 100;
    private static final int SINGLE_ROW_DATA_CODE = 101;
    private static final UriMatcher URI_MATCHER = getUriMatcher();
    private MoviesHelper mHelper;

    public static UriMatcher getUriMatcher(){
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH,DIRECTORY_CODE );
        matcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH + "/#", SINGLE_ROW_DATA_CODE);

        return matcher;
    }
    @Override
    public boolean onCreate() {
        mHelper = new MoviesHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mHelper.getReadableDatabase();
        int match = URI_MATCHER.match(uri);
        Cursor cursor = null;

        switch (match){
            case SINGLE_ROW_DATA_CODE:
                String s = uri.getPathSegments().get(1);
                cursor = database.query(MoviesContract.MoviesEntry.TABLE_NAME, projection, MoviesContract.MoviesEntry.COLUMN_ID + " =?", new String[]{s}, null, null, sortOrder);
                break;

            case DIRECTORY_CODE:
                cursor = database.query(MoviesContract.MoviesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default: throw new UnsupportedOperationException("Wrong Path");
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase database = mHelper.getWritableDatabase();
        int match = URI_MATCHER.match(uri);
        long numRow;
        Uri uri1 = null;

        switch (match){
            case DIRECTORY_CODE:
                numRow = database.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
                if (numRow > 0){
                   uri1 = ContentUris.withAppendedId(uri, numRow);
                }
                break;
            default:throw   new UnsupportedOperationException("Wrong Path") ;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return  uri1;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        return super.bulkInsert(uri, values);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mHelper.getWritableDatabase();
        int match = URI_MATCHER.match(uri);
        int i;

        switch (match){
            case DIRECTORY_CODE:
                i = database.delete(MoviesContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:throw new UnsupportedOperationException("Wrong Path");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return i;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Wrong Path");
    }
}
