package com.renasoft.popmovies.model;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MovieApi {
    @GET("movie/popular?api_key=41e7f883b667d51a09ef944ed6eb5afc")
    Call<Page> getPopularMovies();

    @GET("movie/top_rated?api_key=41e7f883b667d51a09ef944ed6eb5afc")
    Call<Page> getRatedMovies();
}
