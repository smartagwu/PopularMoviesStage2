package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AGWU SMART ELEZUO on 5/9/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private Context mContext;
    private JSONArray mArray;

    public ReviewAdapter(Context context){
        mContext = context;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = mContext;
        int layout = R.layout.activity_review;
        boolean attachToParent = false;

       LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout, parent, attachToParent);

        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {

        String author = null;
        String content = null;

        JSONObject jsonObject = null;
        try {
            jsonObject = mArray.getJSONObject(position);
            author = jsonObject.getString("author");
            content = jsonObject.getString("content");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.author.setText(author);
        holder.content.setText(content);

        if (position == mArray.length()){
            holder.view.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
       if (mArray == null)return 0;
        return mArray.length();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView content, author;
        private View view;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);

            content = (TextView) itemView.findViewById(R.id.content);
            author = (TextView) itemView.findViewById(R.id.author);
            view = (View) itemView.findViewById(R.id.view5);
        }
    }

    public void setData(JSONArray setData){
       mArray = setData;
        notifyDataSetChanged();
    }
}
