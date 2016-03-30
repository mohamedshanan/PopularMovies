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

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ArrayList<Movie> movies;

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView =  inflater
                .inflate(R.layout.fragment_main, container, false);

        if(showMovies()){
            populateList(rootView);
        } else {
            showAlertDialog(getActivity(), "Connection Problem", "Error Loading Data, Please check you Internet Connection", rootView);
            rootView = inflater.inflate(R.layout.not_connected, container, false);
        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        showMovies();
    }

    private boolean showMovies(){
        try {
            movies = new ArrayList<>();

            MoviesTask moviesTask = new MoviesTask();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

            String sort_method = prefs.getString(getString(R.string.pref_sort_key),getString(R.string.pref_sort_popular));

            String jsonResult = moviesTask.execute(sort_method).get();

            if (jsonResult.equals("")){
                return false;
            }

            movies = new MovieParser().getMoviesData(getContext(), jsonResult);

            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    private void populateList (View rootView){
            RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_movies_List);
            mRecyclerView.setHasFixedSize(true);

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(new MovieAdapter(getContext(), movies, new MovieAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Movie movie) {
                    Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);

                    intent.putExtra("TITLE", movie.getMovie_title());
                    intent.putExtra("P_PATH", movie.getMovie_poster_path());
                    intent.putExtra("RATING", movie.getMovie_rating());
                    intent.putExtra("DATE", movie.getMovie_release_date());
                    intent.putExtra("OVERVIEW", movie.getMovie_overview());

                    startActivity(intent);
                }
            }));
    }

    private void showAlertDialog(final Context context, String title, String message,final View rootView){
        AlertDialog mDialog = new AlertDialog.Builder(context).create();
        mDialog.setTitle(title);
        mDialog.setMessage(message);
        mDialog.setIcon(R.drawable.ic_info_black_24dp);
        mDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which){}
        });
        mDialog.show();
    }
}
