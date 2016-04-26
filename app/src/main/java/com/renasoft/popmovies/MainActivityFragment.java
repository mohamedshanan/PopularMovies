package com.renasoft.popmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.renasoft.popmovies.controller.MovieManager;
import com.renasoft.popmovies.model.MovieApi;
import com.renasoft.popmovies.model.Page;
import com.renasoft.popmovies.model.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The Main fragment containing the movies recyclerView.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    List<Result> movies;
    private View rootView;
    private LayoutInflater mInflater;
    private ViewGroup mContainer;
    private boolean responseFlag = true;
    private RecyclerView mRecyclerView;
    private MovieAdapter movieAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mInflater = inflater;
        mContainer = container;
        rootView =  inflater
                .inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_movies_List);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        runMoviesTask();

    }

    private void runMoviesTask() {


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String display_method = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));


        final MovieApi client = MovieManager.createService(MovieApi.class);

        Call<Page> call;

        if (display_method.equalsIgnoreCase(getString(R.string.pref_sort_rated))) {
            call = client.getRatedMovies();
        } else {
            call = client.getPopularMovies();
        }

        try {
            call.enqueue(new Callback<Page>() {
                @Override
                public void onResponse(Call<Page> call, Response<Page> response) {
                    responseFlag = true;

                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
                    final int columns = getResources().getInteger(R.integer.gallery_columns);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), columns));

                    movieAdapter = new MovieAdapter(getActivity());

                    movies = response.body().getResults();

                    for (int i = 0; i < movies.size(); i++) {
                        movieAdapter.addMovie(movies.get(i));
                    }
                    mRecyclerView.setAdapter(movieAdapter);

                }

                @Override
                public void onFailure(Call<Page> call, Throwable t) {
                    Log.i("Call to API Failed : ", t.getLocalizedMessage());
                    showNotConnected();

                }
            });

        } catch (Exception e) {
            showNotConnected();
        }

    }


    private View showNotConnected() {
        // alert the user with alertDialog to check his network connection
        Utils.showAlertDialog(getActivity(), getString(R.string.no_conn_title), getString(R.string.no_conn_msg), rootView);

        //display the no connection screen (screen holder) with a button to retry to rerun movies task
        rootView = mInflater.inflate(R.layout.not_connected, mContainer, false);

        Button btn_retry = (Button) rootView.findViewById(R.id.btn_retry);
        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onResume();
            }
        });
        return rootView;
    }

}
