package com.renasoft.popmovies;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

/**
 * The Main fragment containing the movies recyclerView.
 */
public class MainActivityFragment extends Fragment {

    ArrayList<Movie> movies;

    View rootView;

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        rootView =  inflater
                .inflate(R.layout.fragment_main, container, false);
        /* Check if the asyncTask runs correctly and returns non-empty json string to prevent app
           from crashing if a network or parsing error happened
            if runMoviesTask returns true forward to populateList
         */
        if(runMoviesTask()){
            populateList(rootView);
        } else {
            // alert the user with alertDialog to check his network connection
            showAlertDialog(getActivity(), "Connection Problem", "Error Loading Data, Please check you Internet Connection and try Again", rootView);

            //display the no connection screen (screen holder) with a button to retry to rerun movies task
            rootView = inflater.inflate(R.layout.not_connected, container, false);

            Button btn_retry = (Button) rootView.findViewById(R.id.btn_retry);

            btn_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(runMoviesTask()){
                        populateList(rootView);
                    }
                }
            });
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(runMoviesTask()){
            populateList(rootView);
        }
    }

    public boolean runMoviesTask(){
        try {
            movies = new ArrayList<>();

            MoviesTask moviesTask = new MoviesTask();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

            String sort_method = prefs.getString(getString(R.string.pref_sort_key),getString(R.string.pref_sort_popular));

            String jsonResult = moviesTask.execute(sort_method).get();

            if (jsonResult.equals("")){
                return false;
            }

            movies = MovieParser.getMoviesData(getContext(), jsonResult);

            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    private void populateList (View rootView){

        // Get a reference to the RecyclerView, and attach new movie adapter to it.
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_movies_List);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(new MovieAdapter(getContext(), movies, new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {

                Intent detailsIntent = new Intent(getActivity(), MovieDetailsActivity.class);

                detailsIntent.putExtra("TITLE", movie.getMovie_title());
                detailsIntent.putExtra("P_PATH", movie.getMovie_poster_path());
                detailsIntent.putExtra("RATING", movie.getMovie_rating());
                detailsIntent.putExtra("DATE", movie.getMovie_release_date());
                detailsIntent.putExtra("OVERVIEW", movie.getMovie_overview());

                startActivity(detailsIntent);
            }
        }));
    }

    private void showAlertDialog(final Context context, String title, String message,final View rootView){

        AlertDialog mDialog = new AlertDialog.Builder(context).create();
        mDialog.setTitle(title);
        mDialog.setMessage(message);
        mDialog.setIcon(R.drawable.ic_info_black_24dp);
        mDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        mDialog.show();
    }


}
