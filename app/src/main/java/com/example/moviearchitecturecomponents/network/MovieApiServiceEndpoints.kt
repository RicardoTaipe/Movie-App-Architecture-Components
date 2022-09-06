package com.example.moviearchitecturecomponents.network

import com.example.moviearchitecturecomponents.network.response.MovieDetail
import com.example.moviearchitecturecomponents.network.response.Movies
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/{release_type}")
    suspend fun getMovies(
        @Path("release_type") type: String,
        @Query("api_key") token: String, @Query("page") page: Int,
    ): Movies

    @GET("movie/{movie_id}?")
    suspend fun getMovieById(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") token: String,
        @Query("append_to_response") append: String,
    ): MovieDetail
}