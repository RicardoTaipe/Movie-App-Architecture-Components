package com.example.moviearchitecturecomponents.util

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

@GlideModule
class MyAppGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val requestOptions = RequestOptions().apply {
            format(DecodeFormat.PREFER_RGB_565)
            disallowHardwareConfig()
        }
        builder.setDefaultRequestOptions(requestOptions)
        builder.setDiskCache(InternalCacheDiskCacheFactory(context))
    }
}