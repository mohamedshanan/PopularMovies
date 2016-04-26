package com.renasoft.popmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.renasoft.popmovies.model.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed on 24/03/2016.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>  {


    final Context mContext;
    private final List<Result> movies;


    public MovieAdapter(Context context) {
        this.movies = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_movie, parent, false);
        return new MovieViewHolder(movieView, mContext);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(movies.get(position), new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Result movie) {

                Intent detailsIntent = new Intent(mContext, MovieDetailsActivity.class);
                detailsIntent.putExtra("ID", movie.getId());
                detailsIntent.putExtra("TITLE", movie.getTitle());
                detailsIntent.putExtra("P_PATH", movie.getPosterPath());
                detailsIntent.putExtra("RATING", movie.getVoteAverage());
                detailsIntent.putExtra("DATE", movie.getReleaseDate());
                detailsIntent.putExtra("OVERVIEW", movie.getOverview());

                mContext.startActivity(detailsIntent);
            }
        });
    }

    public void addMovie(Result result) {
        movies.add(result);
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        void onItemClick(Result movie);
    }

    public static class  MovieViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        public ImageView img;

        public MovieViewHolder(View v, Context context) {
            super(v);
            img = (ImageView) v.findViewById(R.id.grid_item_imageview);
            this.context = context;
        }

        public void bind(final Result movie, final OnItemClickListener listener) {
            Picasso.with(itemView.getContext())
                    .load(movie.getPosterPath())
                    .resize(context.getResources().getInteger(R.integer.img_width), context.getResources().getInteger(R.integer.img_height))
                    .into(img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(movie);
                }
            });
        }

    }
}