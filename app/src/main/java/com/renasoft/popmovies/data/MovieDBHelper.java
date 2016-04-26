package com.renasoft.popmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Mohamed on 23/04/2016.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Result.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieContract.MovieEntry.COLUMN_NAME_MOVIEAPI_ID + " INTEGER UNIQUE NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_NAME_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_NAME_MOVIE_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_NAME_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_NAME_MOVIE_RATING + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_NAME_MOVIE_RELEASE_DATE + " TEXT NOT NULL " +
                " );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }

    public long addMovie(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
    }

    public Cursor getFavorites() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {MovieContract.MovieEntry.COLUMN_NAME_MOVIEAPI_ID,
                MovieContract.MovieEntry.COLUMN_NAME_MOVIE_TITLE,
                MovieContract.MovieEntry.COLUMN_NAME_MOVIE_POSTER_PATH,
                MovieContract.MovieEntry.COLUMN_NAME_MOVIE_OVERVIEW,
                MovieContract.MovieEntry.COLUMN_NAME_MOVIE_RATING,
                MovieContract.MovieEntry.COLUMN_NAME_MOVIE_RELEASE_DATE};

        String sortOrder = MovieContract.MovieEntry.COLUMN_NAME_MOVIE_TITLE + " DESC";
        Cursor c = db.query(
                MovieContract.MovieEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return c;
    }

    public boolean isFavourite(int api_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {MovieContract.MovieEntry.COLUMN_NAME_MOVIEAPI_ID};
        Cursor c = db.query(
                MovieContract.MovieEntry.TABLE_NAME,
                projection,
                MovieContract.MovieEntry.COLUMN_NAME_MOVIEAPI_ID + " = " + api_id,
                null,
                null,
                null,
                null);
        if (c == null) {
            return false;
        } else if (!c.moveToFirst()) {
            c.close();
            Log.i("MovieDBHelper", " Not move to first !");
            return false;
        }
        Log.i("MovieDBHelper", "favourite found");
        return true;
    }

    public boolean removeFavourite(int api_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(MovieContract.MovieEntry.TABLE_NAME, MovieContract.MovieEntry.COLUMN_NAME_MOVIEAPI_ID + " = " + api_id, null) > 0;
    }
}
