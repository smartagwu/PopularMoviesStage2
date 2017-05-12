package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Sync.MoviesContract;
import com.squareup.picasso.Picasso;

/**
 * Created by AGWU SMART ELEZUO on 5/12/2017.
 */

public class FavouriteMoviesAdapter extends RecyclerView.Adapter<FavouriteMoviesAdapter.FavouriteMoviesAdapterViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private String posterPath;

    public FavouriteMoviesAdapter(Context context){

        mContext = context;
    }

    @Override
    public FavouriteMoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID = R.layout.movies_adapter_layout;
        boolean attachToScreenImmediately = false;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutID, parent,attachToScreenImmediately);

        FavouriteMoviesAdapterViewHolder adapterviewHolder = new FavouriteMoviesAdapterViewHolder(view);

        return adapterviewHolder;
    }

    @Override
    public void onBindViewHolder(FavouriteMoviesAdapterViewHolder holder, int position) {

        mCursor.moveToPosition(position);
        int nameIndex = mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE);
        int posterIndex = mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER);
        int ratingIndex = mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RATING);

        String data = mCursor.getString(posterIndex);
        String rating = mCursor.getString(ratingIndex);
        String name = mCursor.getString(nameIndex);


        posterPath = "http://image.tmdb.org/t/p/w185" + data;
        Picasso.with(mContext).load(posterPath).placeholder(R.mipmap.ic_launcher).into(holder.adapterImage);
        holder.rating.setText(rating+"/10");
        holder.name.setText(name);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null)return 0;
        return mCursor.getCount();
    }

    public class FavouriteMoviesAdapterViewHolder extends RecyclerView.ViewHolder {

        public final ImageView adapterImage;
        public final TextView name, rating;

        public FavouriteMoviesAdapterViewHolder(View itemView) {
            super(itemView);

            adapterImage = (ImageView) itemView.findViewById(R.id.imageid);
            name = (TextView) itemView.findViewById(R.id.name);
            rating = (TextView) itemView.findViewById(R.id.rating);
        }
    }

    public void swapCursor(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
    }
}
