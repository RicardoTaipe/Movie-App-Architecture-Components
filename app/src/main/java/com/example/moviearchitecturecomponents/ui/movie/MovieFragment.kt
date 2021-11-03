package com.example.moviearchitecturecomponents.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.moviearchitecturecomponents.MainActivity
import com.example.moviearchitecturecomponents.R
import com.example.moviearchitecturecomponents.databinding.FragmentMovieBinding
import com.example.moviearchitecturecomponents.network.NetworkConstants
import com.example.moviearchitecturecomponents.network.response.Result
import com.example.moviearchitecturecomponents.util.AnimatorUtils
import com.example.moviearchitecturecomponents.util.ImageUtil

class MovieFragment : Fragment() {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var binding: FragmentMovieBinding
    private var selectedMovie: Result? = null
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
        selectedMovie = args.selectedMovie
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()

        ViewCompat.setTransitionName(binding.detailMovieImage, selectedMovie?.id.toString())

        ImageUtil.setImageFromUrl(binding.detailMovieImage,"${NetworkConstants.IMAGE_URL_PATH}${selectedMovie?.backdropPath}")
        ImageUtil.setImageFromUrl(binding.detailBackgroundMovie,"${NetworkConstants.IMAGE_URL_PATH}${selectedMovie?.posterPath}")

        binding.detailMovieTitle.text = selectedMovie?.title
        binding.detailMovieDesc.text = selectedMovie?.overview
        (activity as MainActivity).setActionBarTitle(args.selectedMovie?.title)
        binding.executePendingBindings()

        startPostponedEnterTransition()
        AnimatorUtils.loadAnimation(context, binding.detailBackgroundMovie, R.animator.scale_animator)
        AnimatorUtils.loadAnimation(context, binding.playMovie, R.animator.scale_animator)

        movieViewModel.movie.observe(viewLifecycleOwner,{

        })
    }

}