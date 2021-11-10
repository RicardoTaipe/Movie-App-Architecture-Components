package com.example.moviearchitecturecomponents.ui.movie

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.moviearchitecturecomponents.R
import com.example.moviearchitecturecomponents.databinding.FragmentMovieBinding
import com.example.moviearchitecturecomponents.network.NetworkConstants
import com.example.moviearchitecturecomponents.network.response.Result
import com.example.moviearchitecturecomponents.ui.movie.cast.CastAdapter
import com.example.moviearchitecturecomponents.util.AnimatorUtils
import com.example.moviearchitecturecomponents.util.ImageUtil
import com.google.android.material.transition.MaterialContainerTransform

class MovieFragment : Fragment() {

    private val castAdapter = CastAdapter()

    private val movieViewModel: MovieViewModel by lazy {
        ViewModelProvider(this).get(MovieViewModel::class.java)
    }

    private lateinit var binding: FragmentMovieBinding

    private val selectedMovie: Result? by lazy(LazyThreadSafetyMode.NONE) { args.selectedMovie }

    private val args: MovieFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.material_motion_duration_long_1).toLong()
            scrimColor = Color.TRANSPARENT
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMovieBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //postponeEnterTransition()
        setUpCastObserver()
        ViewCompat.setTransitionName(binding.detailMovieImage, selectedMovie?.id.toString())

        ImageUtil.setImageFromUrl(binding.detailMovieImage,
            "${NetworkConstants.IMAGE_URL_PATH}${selectedMovie?.backdropPath}")
        ImageUtil.setImageFromUrl(binding.detailBackgroundMovie,
            "${NetworkConstants.IMAGE_URL_PATH}${selectedMovie?.posterPath}")

        binding.detailMovieTitle.text = selectedMovie?.title
        binding.detailMovieDesc.text = selectedMovie?.overview
        binding.cast.adapter = castAdapter

        //startPostponedEnterTransition()
        AnimatorUtils.loadAnimation(context,
            binding.detailBackgroundMovie,
            R.animator.scale_animator)
        AnimatorUtils.loadAnimation(context, binding.playMovie, R.animator.scale_animator)
        binding.executePendingBindings()

    }

    private fun setUpCastObserver() {
        movieViewModel.movie.observe(viewLifecycleOwner, {
            castAdapter.dataSet = it.credits?.cast!!
        })
    }

}