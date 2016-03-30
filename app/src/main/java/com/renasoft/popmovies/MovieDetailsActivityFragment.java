package com.renasoft.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsActivityFragment extends Fragment {

    private static String TAG = MovieDetailsActivityFragment.class.getSimpleName();

    public MovieDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        Intent intent = getActivity().getIntent();

        if(intent != null ){
            String movie_title =  intent.getStringExtra("TITLE");
            String movie_p_path =  intent.getStringExtra("P_PATH");
            String movie_date =  intent.getStringExtra("DATE");
            String movie_overview =  intent.getStringExtra("OVERVIEW");
            String movie_rating = intent.getStringExtra("RATING");

            ImageView imgThubnail = (ImageView) view.findViewById(R.id.details_imageview);
            TextView tvTitle = (TextView) view.findViewById(R.id.details_title);
            TextView tvDate = (TextView) view.findViewById(R.id.details_date);
            TextView tvOverview = (TextView) view.findViewById(R.id.details_overview);
            TextView ratingBar = (TextView) view.findViewById(R.id.details_rating);

            Picasso.with(getActivity()).load(movie_p_path).into(imgThubnail);
            tvTitle.setText(movie_title);
            tvDate.setText(movie_date);
            ratingBar.setText(movie_rating);
            tvOverview.setText(movie_overview);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }
}
