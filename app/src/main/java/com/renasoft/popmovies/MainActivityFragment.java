package com.renasoft.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    ArrayList<Movie> movies;
    View rootView;

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
            Utils.showAlertDialog(getActivity(), "Connection Problem", "Error Loading Data, Please check you Internet Connection and try Again", rootView);

            //display the no connection screen (screen holder) with a button to retry to rerun movies task
            rootView = inflater.inflate(R.layout.not_connected, container, false);

            Button btn_retry = (Button) rootView.findViewById(R.id.btn_retry);
            btn_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Utils.showProgressDialog(getActivity(), "Reloading Data", "Please wait until movies data reloaded", true);
                    /*
                    if(runMoviesTask()){
                        populateList(rootView);
                    }

                    Fragment newFragment = new MainActivityFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    // Replace the not_connected layout in the fragment_container view with this fragment (main fragment),
                    // and add the transaction to the back stack
                    transaction.replace(R.id.fragment, newFragment);
                    transaction.addToBackStack(null);

                    // Commit the transaction
                    transaction.commit();
                    */
                    //Utils.dismissProgressDialog();
                }
            });
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (rootView == null) {
            if (runMoviesTask()) {
                populateList(rootView);
            }
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

        //LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        //mRecyclerView.setLayoutManager(mLayoutManager);
        //get the number of columns on the grid based on the orientation of the device
        //1 for portrait and 2 for landscape
        final int columns = getResources().getInteger(R.integer.gallery_columns);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), columns));


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

}
