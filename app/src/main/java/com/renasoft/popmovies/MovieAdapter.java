package com.renasoft.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mohamed on 24/03/2016.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>  {


    private final Context mContext;
    private final ArrayList<Movie> movies;
    private final OnItemClickListener mListener;


    public MovieAdapter(Context mContext, ArrayList<Movie> movies, OnItemClickListener listener){
        this.movies = movies;
        this.mContext = mContext;
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_movie, parent, false);
        return new MovieViewHolder (movieView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(movies.get(position), mListener);
    }




    public static class  MovieViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView title;

        public MovieViewHolder(View v) {
            super(v);
            img = (ImageView) v.findViewById(R.id.grid_item_imageview);
            title = (TextView) v.findViewById(R.id.grid_item_title);
        }

        public void bind (final Movie movie, final OnItemClickListener listener){
            title.setText(movie.getMovie_title());
            Picasso.with(itemView.getContext()).load(movie.getMovie_poster_path()).into(img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(movie);
                }
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }
}