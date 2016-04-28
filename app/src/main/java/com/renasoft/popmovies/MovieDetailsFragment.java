package com.renasoft.popmovies;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.renasoft.popmovies.data.MovieContract;
import com.renasoft.popmovies.data.MovieDBHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {

    private static int SENDER_FLAG_TRAILERS = 0;
    private static int SENDER_FLAG_REVIEWS = 1;
    private static String TAG = MovieDetailsFragment.class.getSimpleName();
    MovieDBHelper dbHelper;
    boolean isFavourite;
    ImageButton fav_btn;
    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;
    private int movie_id;
    private String movie_title;
    private String movie_p_path;
    private String movie_date;
    private String movie_overview;
    private String movie_rating;

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        Bundle arguments = getArguments();
        if (arguments == null) {
            arguments = getActivity().getIntent().getExtras();
        }
        if (null != arguments) {
            Bundle details = arguments;
            movie_id = details.getInt("ID", 0);
            movie_title = details.getString("TITLE");
            movie_p_path = details.getString("P_PATH");
            movie_date = details.getString("DATE");
            movie_overview = details.getString("OVERVIEW");
            movie_rating = details.getString("RATING");

            dbHelper = new MovieDBHelper(getActivity());
            isFavourite = dbHelper.isFavourite(movie_id);


            ImageView imgThubnail = (ImageView) rootView.findViewById(R.id.details_imageview);
            TextView tvTitle = (TextView) rootView.findViewById(R.id.details_title);
            TextView tvDate = (TextView) rootView.findViewById(R.id.details_date);
            TextView tvOverview = (TextView) rootView.findViewById(R.id.details_overview);
            TextView ratingBar = (TextView) rootView.findViewById(R.id.details_rating);
            fav_btn = (ImageButton) rootView.findViewById(R.id.add_to_favourites);
            updateFavouriteState();

            Picasso.with(getActivity()).load(movie_p_path).into(imgThubnail);
            tvTitle.setText(movie_title);
            tvDate.setText(movie_date);
            ratingBar.setText(movie_rating);
            tvOverview.setText(movie_overview);
            fav_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //add favorite movie to db

                    if (isFavourite) {
                        if (dbHelper.removeFavourite(movie_id)) {
                            Toast.makeText(getActivity(), "Favourite Removed", Toast.LENGTH_SHORT).show();
                            isFavourite = false;
                            updateFavouriteState();
                        }

                    } else {
                        ContentValues values = new ContentValues();
                        values.put(MovieContract.MovieEntry.COLUMN_NAME_MOVIEAPI_ID, movie_id);
                        values.put(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_TITLE, movie_title);
                        values.put(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_POSTER_PATH, movie_p_path);
                        values.put(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_OVERVIEW, movie_overview);
                        values.put(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_RATING, movie_rating);
                        values.put(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_RELEASE_DATE, movie_date);

                        dbHelper.addMovie(values);
                        Toast.makeText(getActivity(), "Added to Favourites", Toast.LENGTH_SHORT).show();
                        isFavourite = true;
                        updateFavouriteState();
                    }

                }
            });

            loadTrailers(movie_id);
            populateTrailersList(rootView, getContext());

            loadReviews(movie_id);
            populateReviewsList(rootView);
        }

        return rootView;
    }


    private void loadTrailers(int movie_id) {
        try {
            trailers = new ArrayList<>();
            MoviesTask trailersTask = new MoviesTask(null, SENDER_FLAG_TRAILERS, movie_id);
            String trailersJsonResponse = trailersTask.execute().get();

            trailers = MovieParser.getTrailers(trailersJsonResponse, getActivity());
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    private void loadReviews(int movie_id) {
        try {
            reviews = new ArrayList<>();
            MoviesTask reviewsTask = new MoviesTask(null, SENDER_FLAG_REVIEWS, movie_id);
            String reviewsJsonResponse = reviewsTask.execute().get();

            reviews = MovieParser.getReviews(reviewsJsonResponse);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    private void populateTrailersList(View rootView, final Context context) {
        LinearLayout linear_listview = (LinearLayout) rootView.findViewById(R.id.linear_listview);
        for (int i = 0; i < trailers.size(); i++) {
            LayoutInflater inflater = null;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View trailerItem = inflater.inflate(R.layout.trailer_item, null);
            final Trailer trailer = trailers.get(i);


            TextView trailerTextView = (TextView) trailerItem.findViewById(R.id.trailerName);

            trailerTextView.setText(trailer.getName());

            linear_listview.addView(trailerItem);

            trailerItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    trailer.playTrailer();
                }
            });
        }
    }

    private void populateReviewsList(View rootView) {
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.reviews_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new ReviewAdapter(getContext(), reviews));
    }

    private void updateFavouriteState() {
        if (isFavourite) {
            fav_btn.setImageResource(R.drawable.star_gold);
        } else {
            fav_btn.setImageResource(R.drawable.star_white);
        }
    }
}
