package com.example.moviearchitecturecomponents.util

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.view.View
import androidx.annotation.AnimatorRes

object AnimatorUtils {
    fun loadAnimation(context: Context?, view: View, @AnimatorRes layoutAnimator: Int) {
        (AnimatorInflater.loadAnimator(context, layoutAnimator) as AnimatorSet).apply {
            setTarget(view)
            start()
        }
    }
}

//// Using View Animation (legacy code)
//        val scaleAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_animation)
//        binding.playMovie.startAnimation(scaleAnimation)