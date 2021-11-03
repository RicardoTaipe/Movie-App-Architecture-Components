package com.example.moviearchitecturecomponents.ui.movie

import android.animation.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import androidx.annotation.AnimatorRes
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import androidx.vectordrawable.graphics.drawable.AnimationUtilsCompat
import com.bumptech.glide.Glide
import com.example.moviearchitecturecomponents.MainActivity
import com.example.moviearchitecturecomponents.R
import com.example.moviearchitecturecomponents.databinding.FragmentMovieBinding
import com.example.moviearchitecturecomponents.network.NetworkConstants
import com.example.moviearchitecturecomponents.util.AnimatorUtils
import com.example.moviearchitecturecomponents.util.ImageUtil
import com.google.android.material.animation.AnimatorSetCompat

class MovieFragment : Fragment() {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var binding: FragmentMovieBinding
    val args: MovieFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMovieBinding.inflate(inflater, container, false)
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()

        ViewCompat.setTransitionName(binding.detailMovieImage, args.selectedMovie?.id.toString())

        ImageUtil.setImageFromUrl(binding.detailMovieImage,"${NetworkConstants.IMAGE_URL_PATH}${args.selectedMovie?.backdropPath}")
        ImageUtil.setImageFromUrl(binding.detailBackgroundMovie,"${NetworkConstants.IMAGE_URL_PATH}${args.selectedMovie?.posterPath}")

        binding.detailMovieTitle.text = args.selectedMovie?.title
        binding.detailMovieDesc.text = args.selectedMovie?.overview
        (activity as MainActivity).setActionBarTitle(args.selectedMovie?.title)
        binding.executePendingBindings()

        startPostponedEnterTransition()
        AnimatorUtils.loadAnimation(context, binding.detailBackgroundMovie, R.animator.scale_animator)
        AnimatorUtils.loadAnimation(context, binding.playMovie, R.animator.scale_animator)
    }

}