package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Notify.LaunchServices;
import com.example.android.popularmovies.Sync.MoviesContract;
import com.example.android.popularmovies.Utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DetailScreenActivity extends AppCompatActivity {

    private ImageView poster, favourite;
    private TextView title, plot, rating, date, tapText;
    private String id, title1, backdrop, rating1;
    public static final String BASE_URL_ = "http://api.themoviedb.org/3/movie/";
    public static final String YOUTUBE_BASE_URL_ = "https://www.youtube.com/watch";
    public static final String EXTRA_TITLE_TEXT = "title";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_detail);



        //Set findViewById for layout views

        poster = (ImageView) findViewById(R.id.movieposter);
        title = (TextView) findViewById(R.id.title1);
        rating = (TextView) findViewById(R.id.rating);
        plot = (TextView) findViewById(R.id.plot);
        date = (TextView) findViewById(R.id.date);
        favourite = (ImageView)findViewById(R.id.view2);
        tapText = (TextView) findViewById(R.id.textView2);



        //use getIntent to get data from MainActivity
        Intent intent = getIntent();
        if(intent.hasExtra("Title") || intent.hasExtra("Overview") || intent.hasExtra("Rating") || intent.hasExtra("Date") || intent.hasExtra("Poster") || intent.hasExtra("id") || intent.hasExtra("drop")){

            title1 = intent.getStringExtra("Title");
            String overview1 = intent.getStringExtra("Overview");
            rating1 = intent.getStringExtra("Rating");
            String date1 = intent.getStringExtra("Date");
            String poster1 = intent.getStringExtra("Poster");
            id = intent.getStringExtra("id");
            backdrop = intent.getStringExtra("drop");


            //Load the image with picasso
            Picasso.with(this).load(poster1).into(poster);
            title.setText(title1);
            plot.setText(overview1);
            rating.setText(rating1+"/10");
            date.setText(date1);

            checkIfExist();

        }

        //Set the action bar title to the selected movie's title

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title1);
    }

    //use checkIfExist to know if the selected movie is a favourite

    private void checkIfExist() {
        AsyncTask<Void, Void, Cursor> task;

        task = new AsyncTask<Void, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Void... params) {
               return getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                super.onPostExecute(cursor);

                if (cursor != null) {

                    int count = cursor.getCount();

                    for (int i = 0; i < count; i++) {
                        cursor.moveToPosition(i);
                        int index = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ID);
                        int id1 = cursor.getInt(index);

                        if (id1 == Integer.parseInt(id)){
                            favourite.setColorFilter(ContextCompat.getColor(DetailScreenActivity.this, R.color.colorAccent));
                            tapText.setTextColor(getResources().getColor(android.R.color.white));
                        }

                    }
                }

            }
        };

        task.execute();

    }


    //Method to watch  the movie's triller on youtube or on a web browser

    public void watchTriller(View view){
        String stringUri = BASE_URL_ + id + "/videos";
        final Uri uri = Uri.parse(stringUri).buildUpon()
                .appendQueryParameter(NetworkUtils.API_KEY, NetworkUtils.API_KEY_VALUE)
                .build();

        AsyncTask<Void, Void, JSONObject> async;


        async = new AsyncTask<Void, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(Void... params) {
                URL url = null;
                String s = null;
                JSONObject jsonObject = null;

                try {
                    url = new URL(uri.toString());
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }
                try {
                    s = NetworkUtils.getResponseFromHttpUrl(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    jsonObject = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return jsonObject;
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {

                JSONArray jsonArray = null;
                JSONObject jsonObject1 = null;
                String key = null;

                try {
                    jsonArray = jsonObject.getJSONArray("results");
                    jsonObject1 = jsonArray.getJSONObject(0);
                    key = jsonObject1.getString("key");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Uri uri1 = Uri.parse(YOUTUBE_BASE_URL_).buildUpon()
                        .appendQueryParameter("v", key)
                        .build();

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(uri1);

                if (i.resolveActivity(getPackageManager()) != null){
                    startActivity(i);
                }

            }
        };

        async.execute();


    }


    //Method to launch the ReviewActivity class that populates the recyclerview with reviews

    public void viewReview(View view){

        Intent intent = new Intent(this, ReviewActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, id);
        intent.putExtra(EXTRA_TITLE_TEXT, title1);
        startActivity(intent);

    }



    //Handles favourite button clicks to unselect if favourite and select if not favourite

    public void onClickFavourite(View view){
        AsyncTask<Void, Void, Cursor> task;

        task = new AsyncTask<Void, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Void... params) {
                return getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                super.onPostExecute(cursor);

                if (cursor.getCount() != 0) {

                    int count = cursor.getCount();

                    for (int i = 0; i < count; i++) {
                        cursor.moveToPosition(i);
                        int index = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ID);
                        int id1 = cursor.getInt(index);

                        if (id1 == Integer.parseInt(id)){
                            deleteFavourite();
                        }else{
                            if (i == count-1){
                                addFavourite();
                            }

                        }

                    }
                }else {
                    addFavourite();
                }
            }
        };

        task.execute();

    }




//Addfavourite method, adds your favourites movie to the database an tint the favourite icon

    public void addFavourite(){

        AsyncTask<Void, Void, Uri> async;

        final ContentValues contentValues = new ContentValues();

        contentValues.put(MoviesContract.MoviesEntry.COLUMN_ID, id);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_TITLE, title1);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER, backdrop);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_RATING, rating1);

        async = new AsyncTask<Void, Void, Uri>() {
            Uri uri = null;
            @Override
            protected Uri doInBackground(Void... params) {
                uri =  getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, contentValues);
                return uri;
            }

            @Override
            protected void onPostExecute(Uri uri) {
                super.onPostExecute(uri);

                if (uri.getPathSegments().get(1) != ""){
                    favourite.setColorFilter(ContextCompat.getColor(DetailScreenActivity.this, R.color.colorAccent));
                    tapText.setTextColor(getResources().getColor(android.R.color.white));
                    LaunchServices.startIntentService(DetailScreenActivity.this);
                }else{

                    Toast.makeText(DetailScreenActivity.this, "Could not add favourite", Toast.LENGTH_SHORT);
                }

            }
        };

        async.execute();
    }




    //this method deletes or removes favourite movies from the database and tints the favourite icon

    private void deleteFavourite(){
        AsyncTask<Void, Void, Integer> task;

        task = new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                int row;
                String[] args = new String[]{id};
                row = getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI, MoviesContract.MoviesEntry.COLUMN_ID + "=?", args);
                return row;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);

                if (integer != null){
                    favourite.setColorFilter(ContextCompat.getColor(DetailScreenActivity.this, android.R.color.white));
                    tapText.setTextColor(getResources().getColor(android.R.color.black));
                }else {
                    Toast.makeText(DetailScreenActivity.this, "Failed to remove favourite", Toast.LENGTH_SHORT);
                }
            }
        };

        task.execute();
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            onBackPressed();

            return true;
        }

        return false;
    }

}
