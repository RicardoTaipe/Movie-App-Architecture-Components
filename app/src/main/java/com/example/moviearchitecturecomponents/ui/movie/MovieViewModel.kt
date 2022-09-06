package com.example.moviearchitecturecomponents.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.moviearchitecturecomponents.BuildConfig
import com.example.moviearchitecturecomponents.network.MovieApi
import com.example.moviearchitecturecomponents.network.response.MovieDetail
import com.example.moviearchitecturecomponents.util.FormatterUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class ApiStatus { LOADING, ERROR, DONE }

class MovieViewModel : ViewModel() {

    private val _movie = MutableLiveData<MovieDetail>()
    val movie: LiveData<MovieDetail>
        get() = _movie
    val formattedRuntime = Transformations.map(movie) {
        FormatterUtil.formatRuntime(movie.value)
    }
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun getMovieDetail(movieId: Int) {
        coroutineScope.launch {
            try {
                _status.value = ApiStatus.LOADING
                val movie = MovieApi.retrofitService.getMovieById(movieId,
                    BuildConfig.TOKEN,
                    "videos,credits")
                _movie.value = movie
                _status.value = ApiStatus.DONE
            } catch (t: Throwable) {
                _status.value = ApiStatus.ERROR
                //https://api.themoviedb.org/3/movie/580489?api_key=6{API_KEY}&append_to_response=videos,credits
            }
        }
    }

    override fun onCleared() {
        viewModelJob.cancel()
    }
}