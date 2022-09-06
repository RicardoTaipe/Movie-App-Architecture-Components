package com.example.moviearchitecturecomponents.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.moviearchitecturecomponents.R
import com.example.moviearchitecturecomponents.network.NetworkConstants

object ImageUtil {
    fun setImageFromUrl(imageView: ImageView, url: String) {
        Glide.with(imageView.context)
            .load("${NetworkConstants.IMAGE_URL_PATH}${url}")
            .placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_placeholder)
            .into(imageView)
    }

    fun setCircleImageFromUrl(imageView: ImageView, url: String) {
        Glide.with(imageView.context)
            .load("${NetworkConstants.IMAGE_URL_PATH}${url}")
            .circleCrop()
            .placeholder(R.drawable.rounded_image_placeholder)
            .error(R.drawable.rounded_image_placeholder)
            .into(imageView)
    }
}