package com.example.moviearchitecturecomponents.ui.movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviearchitecturecomponents.BuildConfig
import com.example.moviearchitecturecomponents.network.MovieApi
import com.example.moviearchitecturecomponents.network.response.MovieDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    private val _movie = MutableLiveData<MovieDetail>()
    val movie: LiveData<MovieDetail>
        get() = _movie

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getMovieDetail()
    }

    private fun getMovieDetail() {
        coroutineScope.launch {
            try {
                val movie = MovieApi.retrofitService.getMovieById(580489,BuildConfig.TOKEN, "videos,credits")
                _movie.value = movie
            } catch (t: Throwable) {
                //https://api.themoviedb.org/3/movie/580489?api_key=6{API_KEY}&append_to_response=videos,credits
            }
        }
    }

    override fun onCleared() {
        viewModelJob.cancel()
    }
}