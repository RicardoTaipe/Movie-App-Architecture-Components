package com.example.moviearchitecturecomponents.ui.home

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviearchitecturecomponents.BuildConfig
import com.example.moviearchitecturecomponents.network.MovieApi
import com.example.moviearchitecturecomponents.network.response.Movies
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

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
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
            try {
                val movies = MovieApi.retrofitService.getMovies(POPULAR, BuildConfig.TOKEN, 1)
                _movies.value = movies
                Log.d("TAG", movies.toString())
            } catch (t: Throwable) {
                _status.value = "error" + t.message
                Log.d("TAG", t.toString())
            }
        }
    }

    private fun getUpcomingMovies() {
        coroutineScope.launch {
            try {
                val movies = MovieApi.retrofitService.getMovies(UPCOMING, BuildConfig.TOKEN, 1)
                _upcomingMovies.value = movies
                Log.d("TAG", movies.toString())
            } catch (t: Throwable) {
                _status.value = "error" + t.message
            }
        }
    }

    override fun onCleared() {
        viewModelJob.cancel()
        timer.cancel()
    }
}