package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.Utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ReviewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private ReviewAdapter adapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorText;
    private  String intentId;
    private static final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT) && intent.hasExtra(DetailScreenActivity.EXTRA_TITLE_TEXT)){
            intentId = intent.getStringExtra(Intent.EXTRA_TEXT);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(intent.getStringExtra(DetailScreenActivity.EXTRA_TITLE_TEXT) + " Reviews");
        }

        recyclerView = (RecyclerView) findViewById(R.id.Recyclerid);
        progressBar = (ProgressBar) findViewById(R.id.progressbarid);
        errorText = (TextView) findViewById(R.id.Errortextid);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new ReviewAdapter(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
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

    private void onError(){
        recyclerView.setVisibility(View.INVISIBLE);
        errorText.setVisibility(View.VISIBLE);
    }
    private void displayRecyclerView(){
        recyclerView.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.INVISIBLE);
    }

    @Override
    public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<JSONObject>(this) {

            JSONObject array;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                progressBar.setVisibility(View.VISIBLE);

                if (array != null){
                    deliverResult(array);
                }else {
                    forceLoad();
                }
            }

            @Override
            public JSONObject loadInBackground() {
                URL url = null;
                String s = null;
                JSONObject jsonObject = null;
                String stringUri = DetailScreenActivity.BASE_URL_ + intentId + "/reviews";

                Uri uri = Uri.parse(stringUri).buildUpon()
                        .appendQueryParameter(NetworkUtils.API_KEY, NetworkUtils.API_KEY_VALUE)
                        .build();
                try {
                    url = new URL(uri.toString());
                } catch (MalformedURLException e) {
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
            public void deliverResult(JSONObject data) {
                super.deliverResult(data);
                array = data;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {

        progressBar.setVisibility(View.INVISIBLE);

        if (data != null) {

            JSONArray jsonArray = null;

            try {
                jsonArray = data.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            displayRecyclerView();
            adapter.setData(jsonArray);
        }

    }

    @Override
    public void onLoaderReset(Loader<JSONObject> loader) {

    }
}
