package com.example.moviearchitecturecomponents.ui.home

import android.widget.ImageView
import com.example.moviearchitecturecomponents.network.response.Result

class MovieClickListener(val clickListener: (movie: Result, imageView: ImageView) -> Unit) {
    fun onClick(movie: Result, imageView: ImageView) = clickListener(movie, imageView)
}