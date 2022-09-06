package com.example.moviearchitecturecomponents.util

import com.example.moviearchitecturecomponents.network.response.MovieDetail

object FormatterUtil {
    fun formatRuntime(movie: MovieDetail?): String {
        movie?.runtime?.let {
            return "${getHour(it)}h ${getMinutes(it)}min"
        }
        return ""
    }

    private fun getHour(time: Int) = time / 60
    private fun getMinutes(time: Int) = time % 60

}
