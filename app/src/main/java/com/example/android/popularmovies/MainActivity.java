package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Notify.LaunchServices;
import com.example.android.popularmovies.Sync.MoviesContract;
import com.example.android.popularmovies.Utilities.NetworkUtils;
import com.example.android.popularmovies.Utilities.SettingsActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesItemClickListener, LoaderManager.LoaderCallbacks<JSONObject>, SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView recyclerView;
    private TextView errorText;
    private ProgressBar progressBar;
    private MoviesAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private ConnectivityManager manager;

    private static final int POPULAR_MOVIES_LOADER_ID = 1;
    private static final int TOP_RATED_MOVIES_LOADER_ID = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.Recyclerid);
        errorText = (TextView) findViewById(R.id.Errortextid);
        progressBar = (ProgressBar) findViewById(R.id.progressbarid);
        ActionBar actionBar = getSupportActionBar();

            gridLayoutManager = new GridLayoutManager(this, 3);


        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new MoviesAdapter(this, this);
        recyclerView.setAdapter(adapter);

        //get the default preference value and load data based on previous preferred sort order

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        String s = sharedPreferences.getString(getResources().getString(R.string.list_preference_key), "");

        if (manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected()){

            if (s.equals(getResources().getString(R.string.show_popular_movies_value))) {
                displayRecyclerView();
                getSupportLoaderManager().initLoader(POPULAR_MOVIES_LOADER_ID, null, this);
                actionBar.setTitle(R.string.app_name);

            }else if (s.equals(getResources().getString(R.string.Show_top_rated_movies_value))) {
                displayRecyclerView();
                getSupportLoaderManager().initLoader(TOP_RATED_MOVIES_LOADER_ID, null, this);
                actionBar.setTitle("Top Rated Movies");

            }else if (s.equals(getResources().getString(R.string.show_favourite_value))) {

                Intent intent = new Intent(this, FavouriteMovies.class);
                startActivity(intent);

            }else{onError();}

        }else { onError();}

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    public void onError(){
        errorText.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    public void ShowProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

    }

    public void displayRecyclerView(){
        errorText.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMoviesItemClick(int position, JSONArray array) {
        if(array != null) {
            try {
                JSONObject object = array.getJSONObject(position);

                String title = object.getString("title");
                String overview = object.getString("overview");
                String rating = object.getString("vote_average");
                String date = object.getString("release_date");
                String id = object.getString("id");
                String poster = "http://image.tmdb.org/t/p/w780" + object.getString("backdrop_path");
                String backdrop = "http://image.tmdb.org/t/p/w185" +object.getString("poster_path");

                Intent intent = new Intent(this, DetailScreenActivity.class);
                intent.putExtra("Title", title);
                intent.putExtra("Overview", overview);
                intent.putExtra("Rating", rating);
                intent.putExtra("Date", date);
                intent.putExtra("Poster", poster);
                intent.putExtra("id", id);
                intent.putExtra("drop", backdrop);

                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //Here the network calls to retrieve data is made in background using loaders with
    //seperate loader ids for the different sort orders

    @Override
    public Loader<JSONObject> onCreateLoader(final int id, Bundle args) {
        return new AsyncTaskLoader<JSONObject>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                ShowProgressBar();

                forceLoad();
            }

            @Override
            public JSONObject loadInBackground() {
                String s = null;
                JSONObject jsonObject = null;
                String category= null;

                if (id == POPULAR_MOVIES_LOADER_ID){
                    category = NetworkUtils.POPULAR_MOVIES_ID;

                }else if(id ==TOP_RATED_MOVIES_LOADER_ID){
                    category = NetworkUtils.TOP_RATED_MOVIES_ID;
                }

                URL url = NetworkUtils.buildUrl(category);
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
                return jsonObject  ;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {

        progressBar.setVisibility(View.INVISIBLE);
        if (data != null){
            displayRecyclerView();
            JSONArray jsonArray = null;

            try {

                jsonArray = data.getJSONArray("results");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            adapter.setData(jsonArray);
        }else {

            onError();
        }

    }

    @Override
    public void onLoaderReset(Loader<JSONObject> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = R.id.sort_order;

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (item.getItemId() == i){
            Intent setting = new Intent(this, SettingsActivity.class);
            startActivity(setting);

            return true;
        }
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        ActionBar actionBar = getSupportActionBar();

        String s = sharedPreferences.getString(key, "");
        if (s.equals(getResources().getString(R.string.show_popular_movies_value))) {

            if (manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected()) {
                getSupportLoaderManager().initLoader(POPULAR_MOVIES_LOADER_ID, null, this);
                actionBar.setTitle(R.string.app_name);
            }else { onError();}

        }else if (s.equals(getResources().getString(R.string.Show_top_rated_movies_value))) {

            if (manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected()) {
                getSupportLoaderManager().initLoader(TOP_RATED_MOVIES_LOADER_ID, null, this);
                actionBar.setTitle("Top Rated Movies");
            }else { onError();}

        }else if (s.equals(getResources().getString(R.string.show_favourite_value))) {
            Intent intent = new Intent(this, FavouriteMovies.class);
            actionBar.setTitle("Favourite Movies Collection");
            startActivity(intent);
        }else{onError();}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}
