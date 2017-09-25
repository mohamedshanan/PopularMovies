package com.renasoft.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mohamed on 20/04/2016.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {


    private final Context mContext;
    private final ArrayList<Review> reviews;


    public ReviewAdapter(Context mContext, ArrayList<Review> reviews) {
        this.reviews = reviews;
        this.mContext = mContext;
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View reviewView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(reviewView);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.author.setText(reviews.get(position).getAuthor());
        holder.content.setText(reviews.get(position).getContent());
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        public TextView author;
        public TextView content;

        public ReviewViewHolder(View v) {
            super(v);
            author = (TextView) v.findViewById(R.id.review_author);
            content = (TextView) v.findViewById(R.id.review_content);
        }
    }
}