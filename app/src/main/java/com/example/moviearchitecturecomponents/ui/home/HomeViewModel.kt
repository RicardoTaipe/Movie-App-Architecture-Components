package com.example.moviearchitecturecomponents.ui.home

import android.os.CountDownTimer
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

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getMovies()
        timer = object : CountDownTimer(Long.MAX_VALUE, TWO_SECONDS) {
            override fun onTick(millisUntilFinished: Long) {
                if (_page.value == SPLIT_INDEX) {
                    _page.value = -1
                }
                _page.value = _page.value?.plus(1)
            }

            override fun onFinish() {}
        }
        timer.start()
    }

    private fun getMovies() {
        coroutineScope.launch {
            try {
                val movies = MovieApi.retrofitService.getMovies(BuildConfig.TOKEN, 1)
                _movies.value = movies
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