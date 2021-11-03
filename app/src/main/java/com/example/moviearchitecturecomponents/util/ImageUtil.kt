package com.example.moviearchitecturecomponents.util

import android.app.Application
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.moviearchitecturecomponents.R

object ImageUtil {
    fun setImageFromUrl(imageView: ImageView, url: String){
        Glide.with(imageView.context)
            .load(url)
            .placeholder(R.drawable.image_placeholder)
            .into(imageView)
    }
}