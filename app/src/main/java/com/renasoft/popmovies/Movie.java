package com.renasoft.popmovies;

/**
 * Created by Mohamed on 16/03/2016.
 */
public class Movie {
    private int id;
    private String movie_title;
    private String movie_poster_path;
    private String movie_overview;
    private String movie_rating;
    private String movie_release_date;

    Movie(int id, String movie_title, String movie_poster_path, String movie_overview, String movie_rating, String movie_release_date){
        this.id = id;
        this.movie_title = movie_title;
        this.movie_poster_path = movie_poster_path;
        this.movie_overview = movie_overview;
        this.movie_rating = movie_rating;
        this.movie_release_date = movie_release_date;
    }

    public int getId() {
        return id;
    }

    public String getMovie_title() {
        return movie_title;
    }

    public String getMovie_poster_path() {
        return movie_poster_path;
    }

    public String getMovie_overview() {
        return movie_overview;
    }

    public String getMovie_rating() {
        return movie_rating;
    }

    public String getMovie_release_date() {
        return movie_release_date;
    }
}
