package com.example.moviearchitecturecomponents.network

import com.example.moviearchitecturecomponents.network.response.Movie
import com.example.moviearchitecturecomponents.network.response.Movies
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/popular")
    suspend fun getMovies(
        @Query("api_key") token: String, @Query("page") page: Int
    ): Movies

    @GET("movie/{movie_id}?")
    suspend fun getMovie(
        @Path("movie_id") movie_id: Int, @Query("api_key") token: String
    ): Movie
}