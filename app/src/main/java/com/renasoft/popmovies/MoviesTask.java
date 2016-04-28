package com.renasoft.popmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * AsyncTask class to get data from TheMovieDB api
 */
public class MoviesTask extends AsyncTask<String, Void, String> {

    private final String LOG_TAG = MoviesTask.class.getSimpleName();
    private final int MOVIE_URI_POPULAR = 0;
    private final int MOVIE_URI_MOST_RATED = 1;
    private final int MOVIE_URI_TRAILERS = 2;
    private final int MOVIE_URI_REVIEWS = 3;
    String display_method;
    int trailerOrReview;
    int movie_id;

    public MoviesTask(String displayMode, int trailerOrReview, int movie_id) {
        this.display_method = displayMode;
        this.trailerOrReview = trailerOrReview;
        this.movie_id = movie_id;
    }
    @Override
    protected String doInBackground(String... params) {
        String path = "";
        if (display_method != null) {
            if (display_method.equalsIgnoreCase("top_rated"))
                path = buildUri(MOVIE_URI_MOST_RATED);
            else if (display_method.equalsIgnoreCase("popular"))
                path = buildUri(MOVIE_URI_POPULAR);
        } else {
            if (trailerOrReview == 0)
                path = buildUri(MOVIE_URI_TRAILERS);
            else if (trailerOrReview == 1)
                path = buildUri(MOVIE_URI_REVIEWS);
        }
        return getDataJson(path);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
    }

    private String buildUri(int sender_code) {
        // popular Movies  URL("http://api.themoviedb.org/3/movie/popular?api_key=41e7f883b667d51a09ef944ed6eb5afc");
        // top-rated Movies URL("http://api.themoviedb.org/3/movie/top_rated?api_key=your_key");
        // Movie trailers  http://api.themoviedb.org/3/movie/140607/videos?api_key=your_key"
        // Movie reviews  http://api.themoviedb.org/3/movie/140607/reviews?api_key=your_key

        final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
        final String APP_KEY_PARAM = "api_key";
        String path;
        switch (sender_code) {
            case MOVIE_URI_POPULAR:
                path = "popular";
                break;
            case MOVIE_URI_MOST_RATED:
                path = "top_rated";
                break;
            case MOVIE_URI_TRAILERS:
                path = "" + movie_id + "/videos";
                break;
            case MOVIE_URI_REVIEWS:
                path = "" + movie_id + "/reviews";
                break;
            default:
                path = "popular";
        }

        Uri builtUri = Uri.parse(MOVIES_BASE_URL + path).buildUpon()
                .appendQueryParameter(APP_KEY_PARAM, BuildConfig.MyApiKey).build();
        return builtUri.toString();
    }

    private String getDataJson(String path) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        String moviesJsonStr;
        URL url;
        try {
            url = new URL(path);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try {
                connection.connect();
            }
            catch (Exception e){
                Log.e(LOG_TAG, "Connection Problem : " + e.getMessage());
            }

            InputStream inputStream = connection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if(inputStream == null){
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null){
                buffer.append(line + "\n");
            }

            if(buffer.length() == 0){
                return null;
            }

            moviesJsonStr = buffer.toString();
            return moviesJsonStr;
        } catch (IOException ex) {
            Log.e(LOG_TAG, "Error: ", ex);
            return null;
        }
        finally {
            if(connection !=null){
                connection.disconnect();
            }
            if(reader != null){
                try{
                    reader.close();
                }catch (IOException ex){
                    Log.e(LOG_TAG, "Error closing stream", ex);
                }
            }
        }
    }

}
