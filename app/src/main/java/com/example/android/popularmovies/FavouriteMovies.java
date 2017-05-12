package com.example.android.popularmovies;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.Sync.MoviesContract;

/**
 * Created by AGWU SMART ELEZUO on 5/12/2017.
 */

public class FavouriteMovies extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView recyclerView;
    private TextView errorText;
    private ProgressBar progressBar;
    private FavouriteMoviesAdapter adapter;
    private GridLayoutManager gridLayoutManager;

    private static final int CURSOR_LOADER_ID = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView) findViewById(R.id.Recyclerid);
        errorText = (TextView) findViewById(R.id.Errortextid);
        progressBar = (ProgressBar) findViewById(R.id.progressbarid);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Favourite Movies Collection");
        actionBar.setDisplayHomeAsUpEnabled(true);

        gridLayoutManager = new GridLayoutManager(this, 3);


        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new FavouriteMoviesAdapter(this);
        recyclerView.setAdapter(adapter);

        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        ShowProgressBar();
        return new CursorLoader(this,
                MoviesContract.MoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null) {
            progressBar.setVisibility(View.INVISIBLE);
            displayRecyclerView();
            adapter.swapCursor(data);
        }else {
            onError();
            errorText.setText("No favourite movie added yet!");
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
