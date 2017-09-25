package com.renasoft.popmovies.data;

import android.provider.BaseColumns;

/**
 * Created by Mohamed on 23/04/2016.
 */
public class MovieContract {

    public static abstract class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_NAME_MOVIEAPI_ID = "api_id";
        public static final String COLUMN_NAME_MOVIE_TITLE = "title";
        public static final String COLUMN_NAME_MOVIE_POSTER_PATH = "poster_path";
        public static final String COLUMN_NAME_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_NAME_MOVIE_RATING = "rating";
        public static final String COLUMN_NAME_MOVIE_RELEASE_DATE = "date";
    }
}
