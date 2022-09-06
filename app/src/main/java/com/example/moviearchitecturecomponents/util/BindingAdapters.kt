package com.example.moviearchitecturecomponents.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("imageUrl")
fun bindImage(view: ImageView, imgUrl: String?) {
    imgUrl?.let { ImageUtil.setImageFromUrl(view, it) }
}

@BindingAdapter("roundedImageUrl")
fun roundedImage(view: ImageView, imgUrl: String?) {
    imgUrl?.let { ImageUtil.setCircleImageFromUrl(view, it) }
}