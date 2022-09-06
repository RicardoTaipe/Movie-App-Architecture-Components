package com.example.moviearchitecturecomponents.ui.movie

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.moviearchitecturecomponents.R
import com.example.moviearchitecturecomponents.databinding.FragmentMovieBinding
import com.example.moviearchitecturecomponents.network.response.Genre
import com.example.moviearchitecturecomponents.network.response.MovieDetail
import com.example.moviearchitecturecomponents.network.response.Result
import com.example.moviearchitecturecomponents.ui.movie.cast.CastAdapter
import com.example.moviearchitecturecomponents.ui.videoplayer.VideoPlayerFragment
import com.example.moviearchitecturecomponents.util.AnimatorUtils
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform


class MovieFragment : Fragment() {

    companion object {
        const val YOUTUBE = "YouTube"
        const val TRAILER = "Trailer"
    }

    private val castAdapter = CastAdapter()

    private val movieViewModel: MovieViewModel by lazy {
        ViewModelProvider(this)[MovieViewModel::class.java]
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
    ): View {
        binding = FragmentMovieBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = movieViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieViewModel.getMovieDetail(selectedMovie?.id!!)
        ViewCompat.setTransitionName(binding.detailMovieImage, selectedMovie?.id.toString())
        binding.cast.adapter = castAdapter
        setUpCastObserver()
        setNavigationUpListener()
        setFavoriteIconListener()
        toggleDescriptionSection()
        openTrailer()
    }

    private fun openTrailer() {
        binding.playMovie.setOnClickListener {
            val videos = movieViewModel.movie.value?.videos?.results?.filter { resultX ->
                resultX.site.equals(YOUTUBE) && resultX.official == true && resultX.type.equals(
                    TRAILER)
            } ?: return@setOnClickListener
            val video = videos[0]
            video.key?.let {
                VideoPlayerFragment.newInstance(it)
                    .show(parentFragmentManager, "dialog")
            }
        }
    }

    private fun setFavoriteIconListener() {
        binding.favorite.setOnCheckedChangeListener { _, isChecked ->
            AnimatorUtils.loadAnimation(context, binding.favorite, R.animator.heart_animation)
            val message = if (isChecked) {
                R.string.ADDED_TO_FAVORITES
            } else {
                R.string.REMOVED_FROM_FAVORITES
            }
            Snackbar.make(requireActivity().findViewById(R.id.container),
                getString(message),
                Snackbar.LENGTH_SHORT)
                .setAnchorView(requireActivity().findViewById(R.id.nav_view))
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))
                .show()
        }
    }

    private fun setNavigationUpListener() {
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
    }

    private fun toggleDescriptionSection() {
        val initialHeight = binding.detailMovieDesc.layoutParams.height
        binding.toggleDescription.setOnCheckedChangeListener { _, isChecked ->
            TransitionManager.beginDelayedTransition(binding.movieContainer, AutoTransition())
            val params = binding.detailMovieDesc.layoutParams
            val text = if (isChecked) {
                params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                R.string.Less
            } else {
                params.height = initialHeight
                R.string.More
            }
            binding.toggleDescription.text = getString(text)
            binding.detailMovieDesc.layoutParams = params
        }
    }

    private fun setUpCastObserver() {
        movieViewModel.movie.observe(viewLifecycleOwner) {
            bindData(it)
        }
    }

    private fun bindData(movie: MovieDetail?) {
        movie?.credits?.cast?.let {
            castAdapter.submitList(if (it.size > 10) it.take(10) else it)
            if (it.size > 3) {
                AnimatorUtils.loadAnimation(context, binding.cast, R.animator.peekaboo)
            }
        }

        binding.detailMovieGenres.removeAllViews()
        movie?.genres?.forEach { genre ->
            binding.detailMovieGenres.addView(generateChip(genre))
        }
    }

    private fun generateChip(genre: Genre): Chip {
        val inflater = LayoutInflater.from(binding.detailMovieGenres.context)
        val chip: Chip =
            inflater.inflate(R.layout.category_chip, binding.detailMovieGenres, false) as Chip
        chip.text = genre.name
        return chip
    }
}
