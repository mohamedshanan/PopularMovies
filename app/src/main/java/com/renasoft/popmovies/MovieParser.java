package com.renasoft.popmovies;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mohamed on 16/03/2016.
 */
public class MovieParser {
    public static ArrayList<Movie> getMoviesData(Context context, String jsonStr) throws JSONException {

        ArrayList<Movie> movies = new ArrayList<>();

        final String TMDB_ID = "id";
        final String TMDB_RESULTS = "results";
        final String TMDB_TITLE = "original_title";
        final String TMDB_POSTER_PATH = "poster_path";
        final String TMDB_OVERVIEW = "overview";
        final String TMDB_VOTE = "vote_average";
        final String TMDB_DATE = "release_date";

        final String PICASSO_BASE = "http://image.tmdb.org/t/p/w342";

        JSONObject responseJson = new JSONObject(jsonStr);
        JSONArray moviesArray = responseJson.getJSONArray(TMDB_RESULTS);

        int id;
        String title, poster_path, overview, date, vote_average;
        JSONObject movieJson;

        for(int i = 0; i < moviesArray.length(); i++){

            movieJson = moviesArray.getJSONObject(i);

            id = movieJson.getInt(TMDB_ID);
            title = movieJson.getString(TMDB_TITLE);
            poster_path = PICASSO_BASE + movieJson.getString(TMDB_POSTER_PATH);
            overview = movieJson.getString(TMDB_OVERVIEW);
            vote_average = movieJson.getString(TMDB_VOTE);
            date = movieJson.getString(TMDB_DATE);

            movies.add(new Movie(id, title, poster_path, overview, vote_average, date));
        }

        return movies;
    }

}
