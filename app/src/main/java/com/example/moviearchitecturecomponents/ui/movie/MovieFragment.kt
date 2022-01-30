package com.example.moviearchitecturecomponents.ui.movie

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
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
import com.example.moviearchitecturecomponents.network.response.Genre
import com.example.moviearchitecturecomponents.network.response.MovieDetail
import com.example.moviearchitecturecomponents.network.response.Result
import com.example.moviearchitecturecomponents.ui.movie.cast.CastAdapter
import com.example.moviearchitecturecomponents.util.AnimatorUtils
import com.example.moviearchitecturecomponents.util.ImageUtil
import com.google.android.material.chip.Chip
import com.google.android.material.transition.MaterialContainerTransform

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.moviearchitecturecomponents.ui.videoplayer.VideoPlayerFragment
import com.google.android.material.snackbar.Snackbar


class MovieFragment : Fragment() {

    companion object {
        const val YOUTUBE = "YouTube"
        const val TRAILER = "Trailer"
    }

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
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
        setUpCastObserver()
        movieViewModel.getMovieDetail(selectedMovie?.id!!)
        ViewCompat.setTransitionName(binding.detailMovieImage, selectedMovie?.id.toString())
        binding.cast.adapter = castAdapter

        loadImages()

        binding.playMovie.setOnClickListener {
            val videos = movieViewModel.movie.value?.videos?.results?.filter { resultX ->
                resultX.site.equals(YOUTUBE) && resultX.official == true && resultX.type.equals(
                    TRAILER)
            }
            val video = videos?.get(0) ?: return@setOnClickListener
            video.key.let {
                VideoPlayerFragment.newInstance(it!!)
                    .show(childFragmentManager, "dialog")
            }
        }

        binding.favorite.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) {
                getString(R.string.ADDED_TO_FAVORITES)
            } else {
                getString(R.string.REMOVED_FROM_FAVORITES)
            }
            AnimatorUtils.loadAnimation(context, binding.favorite, R.animator.heart_animation)
            Snackbar.make(requireActivity().findViewById(R.id.container),
                message,
                Snackbar.LENGTH_SHORT)
                .setAnchorView(requireActivity().findViewById(R.id.nav_view))
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))
                .show()
        }
        toggleDescriptionSection()

        binding.detailMovieTitle.text = selectedMovie?.originalTitle
        binding.detailMovieDesc.text = selectedMovie?.overview

        AnimatorUtils.loadAnimation(context, binding.detailMovieDesc, R.animator.scale_animator)
        AnimatorUtils.loadAnimation(context, binding.playMovie, R.animator.scale_animator)

        binding.executePendingBindings()

    }

    private fun loadImages() {
        ImageUtil.setImageFromUrl(binding.detailMovieImage,
            "${NetworkConstants.IMAGE_URL_PATH}${selectedMovie?.backdropPath}")
        ImageUtil.setImageFromUrl(binding.detailBackgroundMovie,
            "${NetworkConstants.IMAGE_URL_PATH}${selectedMovie?.posterPath}")
    }

    private fun toggleDescriptionSection() {
        val initialHeight = binding.detailMovieDesc.layoutParams.height
        binding.toggleDescription.setOnCheckedChangeListener { _, isChecked ->
            TransitionManager.beginDelayedTransition(binding.movieContainer, AutoTransition())
            val params = binding.detailMovieDesc.layoutParams
            val text: String
            if (isChecked) {
                params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                text = getString(R.string.Less)
            } else {
                params.height = initialHeight
                text = getString(R.string.More)
            }
            binding.toggleDescription.text = text
            binding.detailMovieDesc.layoutParams = params
        }
    }

    private fun setUpCastObserver() {
        movieViewModel.movie.observe(viewLifecycleOwner, {
            bindData(it)
        })
    }

    private fun bindData(movie: MovieDetail?) {
        movie?.credits?.cast?.let {
            castAdapter.dataSet = it
        }

        movie?.runtime?.let {
            binding.detailMovieRuntime.text = getString(R.string.movie_runtime,
                getHour(it),
                getMinutes(it)
            )
        }
        movie?.genres?.forEach { genre ->
            binding.detailMovieGenres.addView(generateChip(genre))
        }
        movie?.releaseDate?.let {
            binding.detailMovieReleaseDate.text = it
        }
    }

    private fun generateChip(genre: Genre): Chip {
        return Chip(context).apply {
            text = genre.name
        }
    }

    private fun getHour(time: Int) = time / 60

    private fun getMinutes(time: Int) = time % 60

}