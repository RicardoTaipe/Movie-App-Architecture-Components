package com.example.moviearchitecturecomponents.ui.home

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviearchitecturecomponents.BuildConfig
import com.example.moviearchitecturecomponents.network.MovieApi
import com.example.moviearchitecturecomponents.network.response.Movies
import com.example.moviearchitecturecomponents.ui.movie.ApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    companion object {
        private const val TWO_SECONDS = 2000L
        const val SPLIT_INDEX = 5
        const val POPULAR = "popular"
        const val UPCOMING = "upcoming"
    }

    private val timer: CountDownTimer

    private val _page = MutableLiveData(-1)
    val page: LiveData<Int>
        get() = _page

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val _movies = MutableLiveData<Movies>()
    val movies: LiveData<Movies>
        get() = _movies

    private val _upcomingMovies = MutableLiveData<Movies>()
    val upcomingMovies: LiveData<Movies>
        get() = _upcomingMovies

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getPopularMovies()
        getUpcomingMovies()
        timer = setUpCountDownTimer()
        timer.start()
    }

    private fun setUpCountDownTimer() = object : CountDownTimer(Long.MAX_VALUE, TWO_SECONDS) {
        override fun onTick(millisUntilFinished: Long) {
            if (_page.value == SPLIT_INDEX) {
                _page.value = 0
                return
            }
            _page.value = _page.value?.plus(1)
        }

        override fun onFinish() {}
    }

    private fun getPopularMovies() {
        coroutineScope.launch {
            _movies.value = retrieveMovies(POPULAR)
        }
    }

    private fun getUpcomingMovies() {
        coroutineScope.launch {
            _upcomingMovies.value = retrieveMovies(UPCOMING)
        }
    }

    private suspend fun retrieveMovies(movieType: String): Movies? {
        return try {
            _status.value = ApiStatus.LOADING
            val movies = MovieApi.retrofitService.getMovies(movieType, BuildConfig.TOKEN, 1)
            _status.value = ApiStatus.DONE
            movies
        } catch (t: Throwable) {
            _status.value = ApiStatus.ERROR
            Log.e("TAG", t.toString())
            null
        }

    }

    override fun onCleared() {
        viewModelJob.cancel()
        timer.cancel()
    }
}