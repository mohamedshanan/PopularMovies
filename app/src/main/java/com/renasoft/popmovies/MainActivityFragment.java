package com.renasoft.popmovies;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.renasoft.popmovies.data.MovieDBHelper;

import java.util.ArrayList;

/**
 * The Main fragment containing the movies recyclerView.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    ArrayList<Movie> movies;
    View rootView;
    LayoutInflater mInflater;
    ViewGroup mContainer;
    MovieItemListener movieItemListener;
    RecyclerView mRecyclerView;
    TextView no_movies;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {


        rootView =  inflater
                .inflate(R.layout.fragment_main, container, false);

        mInflater = inflater;
        mContainer = container;

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_movies_List);
        no_movies = (TextView) rootView.findViewById(R.id.no_movies_tv);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        no_movies.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

        runMoviesTask();
        populateMoviesList(rootView);

    }

    public void runMoviesTask() {

        movies = new ArrayList<>();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String display_method = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));

        // load favorites
        if (display_method.equals(getString(R.string.pref_sort_favorites))) {
            MovieDBHelper dbHelper = new MovieDBHelper(getActivity());
            Cursor cursor = dbHelper.getFavorites();
            if (cursor == null) {
                OnEmptyList();
            }
            try {
                while (cursor.moveToNext()) {
                    movies.add(new Movie(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5)));
                }
                if (movies.size() == 0) {
                    OnEmptyList();
                }
            } catch (Exception ex) {
                OnEmptyList();
            } finally {
                cursor.close();
            }
        } else {
            try {
                MoviesTask moviesTask = new MoviesTask(display_method, -1, -1);
                String jsonResult = moviesTask.execute().get();

                if (jsonResult.equals("")) {
                    OnEmptyList();
                }

                movies = MovieParser.getMoviesData(getContext(), jsonResult);
            } catch (Exception e) {
                Utils.showAlertDialog(getActivity(),
                        getString(R.string.no_connection_title),
                        getString(R.string.no_connection), rootView);
                OnEmptyList();
            }
        }
    }

    private void populateMoviesList(View rootView) {
        mRecyclerView.setHasFixedSize(true);

        //get the number of columns on the grid based on the orientation of the device
        //1 for portrait and 2 for landscape
        final int columns = getResources().getInteger(R.integer.gallery_columns);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), columns));

        mRecyclerView.setAdapter(new MovieAdapter(getContext(), movies, new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {

                Bundle movieDetails = new Bundle();
                movieDetails.putInt("ID", movie.getId());
                movieDetails.putString("TITLE", movie.getMovie_title());
                movieDetails.putString("P_PATH", movie.getMovie_poster_path());
                movieDetails.putString("RATING", movie.getMovie_rating());
                movieDetails.putString("DATE", movie.getMovie_release_date());
                movieDetails.putString("OVERVIEW", movie.getMovie_overview());

                movieItemListener.setMovieDetails(movieDetails);
            }
        }));
    }

    public void setMovieItemListener(MovieItemListener movieItemListener) {
        this.movieItemListener = movieItemListener;
    }

    public void OnEmptyList() {
        mRecyclerView.setVisibility(View.GONE);
        no_movies.setVisibility(View.VISIBLE);
    }
}
