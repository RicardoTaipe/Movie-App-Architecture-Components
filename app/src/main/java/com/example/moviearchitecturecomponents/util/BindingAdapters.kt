package com.example.moviearchitecturecomponents.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.moviearchitecturecomponents.network.NetworkConstants

@BindingAdapter("imageUrl")
fun bindImage(view: ImageView, imgUrl: String?) {
    imgUrl?.let { ImageUtil.setImageFromUrl(view, it) }
}