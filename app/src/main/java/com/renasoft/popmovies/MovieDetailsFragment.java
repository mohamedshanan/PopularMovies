package com.renasoft.popmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.renasoft.popmovies.data.MovieContract;
import com.renasoft.popmovies.data.MovieDBHelper;
import com.squareup.picasso.Picasso;

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

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);


        Intent intent = getActivity().getIntent();

        if(intent != null ){
            final int movie_id = intent.getIntExtra("ID", 0);
            final String movie_title = intent.getStringExtra("TITLE");
            final String movie_p_path = intent.getStringExtra("P_PATH");
            final String movie_date = intent.getStringExtra("DATE");
            final String movie_overview = intent.getStringExtra("OVERVIEW");
            final String movie_rating = intent.getStringExtra("RATING");

            dbHelper = new MovieDBHelper(getActivity());
            isFavourite = dbHelper.isFavourite(movie_id);


            ImageView imgThubnail = (ImageView) rootView.findViewById(R.id.details_imageview);
            TextView tvTitle = (TextView) rootView.findViewById(R.id.details_title);
            TextView tvDate = (TextView) rootView.findViewById(R.id.details_date);
            TextView tvOverview = (TextView) rootView.findViewById(R.id.details_overview);
            TextView ratingBar = (TextView) rootView.findViewById(R.id.details_rating);
            fav_btn = (ImageButton) rootView.findViewById(R.id.add_to_favourites);

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
                    }
                }
            });
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}
