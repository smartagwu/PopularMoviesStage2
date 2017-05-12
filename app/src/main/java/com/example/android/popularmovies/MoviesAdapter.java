package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.data;

/**
 * Created by AGWU SMART ELEZUO on 4/8/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterviewHolder> {

    private JSONArray dataArray;
    private JSONObject jsonObject;
    private String data, posterPath, name, rating;
    private Context getContext;

    private MoviesItemClickListener listener;

    public interface MoviesItemClickListener{
        void onMoviesItemClick(int position, JSONArray array);
    }

    @Override
    public MoviesAdapterviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID = R.layout.movies_adapter_layout;
        boolean attachToScreenImmediately = false;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutID, parent,attachToScreenImmediately);

        MoviesAdapterviewHolder adapterviewHolder = new MoviesAdapterviewHolder(view);

        return adapterviewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesAdapterviewHolder holder, int position) {

        try {
            jsonObject = dataArray.getJSONObject(position);
            data = jsonObject.getString("poster_path");
            name = jsonObject.getString("title");
            rating = jsonObject.getString("vote_average");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        posterPath = "http://image.tmdb.org/t/p/w185" + data;
        Picasso.with(getContext).load(posterPath).into(holder.adapterImage);
        holder.rating.setText(rating+"/10");
        holder.name.setText(name);


    }

    @Override
    public int getItemCount() {
        if(null == dataArray)return 0;
        return dataArray.length();
    }

    public MoviesAdapter(Context context, MoviesItemClickListener clickListener){

        listener = clickListener;
        getContext = context;
    }

    public class MoviesAdapterviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView adapterImage;
        public final TextView name, rating;

        public MoviesAdapterviewHolder(View itemView) {
            super(itemView);
            adapterImage = (ImageView) itemView.findViewById(R.id.imageid);
            name = (TextView) itemView.findViewById(R.id.name);
            rating = (TextView) itemView.findViewById(R.id.rating);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            listener.onMoviesItemClick(getAdapterPosition(), dataArray);
        }
    }

    public void setData(JSONArray setData){

        dataArray = setData;
        notifyDataSetChanged();
    }
}
