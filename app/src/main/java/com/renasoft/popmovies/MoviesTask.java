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
 * Created by Mohamed on 16/03/2016.
 */
public class MoviesTask extends AsyncTask<String, Void, String> {

    private final String LOG_TAG = MoviesTask.class.getSimpleName();

    public interface AsyncResponse{
        void processFinish(String output);
    }

    @Override
    protected String doInBackground(String... params) {
        if(params.length == 0){
            return null;
        }

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        String moviesJsonStr = null;

        String sort = params[0];

        try{
            // URL url = new URL("http://api.themoviedb.org/3/movie/popular?api_key=41e7f883b667d51a09ef944ed6eb5afc");


            final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String APP_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendPath(sort)
                    .appendQueryParameter(APP_KEY_PARAM, BuildConfig.MyApiKey).build();


            URL url = new URL(builtUri.toString());

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


        }   catch (IOException ex){
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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
    }
}
